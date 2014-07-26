This project was origionally forked from https://github.com/sean8223/jooq-sbt-plugin, 
many thanks to Sean for all his hard work, I'm mostly just distributing said work.

This is an SBT plugin that provides an interface to the JOOQ code generation tool
(<http://www.jooq.org>). The plugin is compatible with SBT 0.11.3+ and Scala 2.10.x

The plugin is in the process of being released, and will be released in a few days as version *2.0*

[![Build Status](https://travis-ci.org/kbrowder/jooq-sbt-plugin.svg?branch=release-2.0)](https://travis-ci.org/kbrowder/jooq-sbt-plugin)

Quick Start
===========

1. Add jooq-sbt-plugin to your `project/plugins.sbt`:
        
        addSbtPlugin("me.kbrowder" %% "jooq-sbt-plugin" % CURRENT_PLUGIN_VERSION) // see above
		
2. In your `build.sbt`, do the following:

   * Inject the plugin settings into your build definition:
   
            seq(jooqSettings:_*)
			
     This will also add the JOOQ libraries to your application's compile
	 `libraryDependencies`.
			
   * Add your database driver to your list of `libraryDependencies` with "jooq" scope:
   
            libraryDependencies += "mysql" % "mysql-connector-java" % "5.1.22" % "jooq"
			
   * Configure options for your environment:
   
             jooqOptions := Seq("jdbc.driver" -> "com.mysql.jdbc.Driver",
                                "jdbc.url" -> "jdbc:mysql://localhost:3306/fnord",
                                "jdbc.user" -> "fnord",
                                "jdbc.password" -> "fnord",
                                "generator.database.name" -> "org.jooq.util.mysql.MySQLDatabase",
                                "generator.database.inputSchema" -> "fnord",
                                "generator.target.packageName" -> "com.myproject.jooq")
			 
			 
Settings
========

The plugin exposes several settings:

* *jooq-options* (`jooqOptions` in build.sbt): a `Seq[Tuple2[String, String]]`
  containing configuration properties for the JOOQ code generator. These will 
  be transformed into paths into the XML configuration file; for example, the option
  `"jdbc.driver" -> "com.mysql.jdbc.Driver"` will be merged into the `<configuration>`
  document as:

            <configuration>
			   <jdbc>
			     <driver>com.mysql.jdbc.Driver</driver>
			   </jdbc>
            </configuration>

  Refer to <http://www.jooq.org/doc/3.0/manual/code-generation/codegen-configuration/>
  for a complete description of JOOQ's configuration options. 

* *jooq-output-directory* (`jooqOutputDirectory` in build.sbt): a `File`
  indicating where JOOQ should deposit the source files it generates. By
  default, this is set to the value of `compile:source-managed` + "/java"
  (usually target/_scala-version_/src_managed/main/java), but it can
  be changed to suit your project layout as needed.

* *jooq-version* (`jooqVersion` in build.sbt): a `String` indicating the version
  of JOOQ to use. The JOOQ libraries at this version will also be imported into your
  project's compile scope. The default value is *3.2.1*, but the plugin is known
  to work with the 2.x series of JOOQ as well (e.g. 2.6.1).

* *jooq-log-level* (`jooqLogLevel` in build.sbt): a `String` controlling the
  the verbosity of code generator's logging. It defaults to "info", which 
  still produces a fair amount of output. Setting it to "error" will effectively
  silence it, except in the case of problems. Other values include "warn" and "debug".


Tasks
=====

And provides a single task:

* *jooq:codegen*: Runs the code generator.

The plugin also attaches to the `compile:compile` task (by way of 
`compile-source-generators`) and will run prior to compile if doesn't see any
`*.java` files in the directory indicated by `jooq-output-directory` (e.g. if
you run `clean`).


Generating Code for Multiple Databases
======================================

It is possible to generate code for multiple databases, although it requires a
more complicated project structure. You'll need to follow the guidelines for 
setting up a [multi-project SBT build.](http://www.scala-sbt.org/release/docs/Getting-Started/Multi-Project.html)

For example, consider the following project structure, that defines three modules:
common code, and JOOQ code generated for two different database types (e.g. Oracle
and MySQL):

    myproject/
        project/
	        plugins.sbt
            MyProject.scala
        myproject-jooq-oracle/
	        build.sbt
        myproject-jooq-mysql/
            build.sbt
        myproject-common/
            build.sbt

To accomplish this:

1. At the root of your project, create the standard `project` directory and place
   a `plugins.sbt` (as described in Quick Start) and a Scala-based build definition
   (named `MyProject.scala` in this example).
   
2. In `MyProject.scala`, define a root project that aggregates the three subprojects.
   For example:

        object MyProject extends Build {
            lazy val root = project.in(file(".")) aggregate(myProjectJooqOracle, myProjectJooqMySQL, myProjectCommon)
            lazy val myProjectJooqOracle = project in file ("jooq-oracle")
            lazy val myProjectJooqMySQL = project in file ("jooq-mysql")
            lazy val myProjectCommon = project.in(file("common")).dependsOn(myProjectJooqOracle, myProjectJooqMySQL)
        }

3. In the `build.sbt` in each of the JOOQ sub-modules, vary the properties as needed to 
   generate code for that database type. Each sub-module will have its own `jooqOptions`
   that you can use to control how the code is generated. You will also specify
   the required database drivers in that module's `libraryDependencies` with
   `jooq` scope as above.

Refer to the SBT documentation for more thorough examples of multi-project build files.



History
=======

* 2.0: First release of me.kbrowder.jooq-sbt-plugin, updated licenses and prepared sbt file for sonatype, added travis configs
