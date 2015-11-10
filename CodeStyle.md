# Introduction #

Wiki page is about rules and coding style in order to keep the source consistent. Mostly we will follow rules defined by [Google for Google Web Toolkit](http://code.google.com/webtoolkit/makinggwtbetter.html#codestyle) and standard [Java code convetions defined by SUN](http://java.sun.com/docs/codeconv/html/CodeConvTOC.doc.html).

# Project packaging #

```
acris-<project-name>
                   /src/main/java/
                                 sk/seges/acris/<project>/
                                                              client
                                                              common
                                                              rpc
                                                              rebind
                                                              server
                   /src/main/resources/
                                 sk/seges/acris/
                                               <project>/<Project>.gwt.xml
                                               <Project>.gwt.xml
                   /src/test/java/
                                  ...
                   /src/test/resources/
                                  ...

```
  1. **client package** - contains specific code for GWT
> > - this package should be included in `<Project>.gwt.xml` using `<source path="client"/>`
  1. **common package** - contains code indenpedent for GWT used also in client side & server side
> > - this package should be included in `<Project>.gwt.xml` using `<source path="common"/>`
  1. **rpc package** - constains code used in RPC call between client and server. Mostly used for services and domain objects
> > - this package should be included in `<Project>.gwt.xml` using `<source path="rpc"/>`
  1. **rebind package** - used for GWT generators. This package wont be used in `<Project>.gwt.xml`
  1. **server package** - pure java code used on server side. This package wont be used in `<Project>.gwt.xml`

# Code style #

  1. Source dependency - when you add acris GWT based dependency, add also source dependency
```
<dependency>
	<groupId>sk.seges.acris</groupId>
	<artifactId>acris-callbacks</artifactId>
</dependency>
<dependency>
	<groupId>sk.seges.acris</groupId>
	<artifactId>acris-callbacks</artifactId>
	<classifier>sources</classifier>
</dependency>
```

  1. GWT Dependency rule - when you add new dependency to the pom.xml you have to also add new inherit to your gwt.xml file (only in case of GWT based dependencies). This will keep projects compilable independently and can be reused in other projects in clean way.
```
<inherits name="sk.seges.acris.Callbacks"/>
```

# Working with code #

Download source code from our SVN. SVN repository is divided into several pieces:
  * **corpis** - base projects for most of the projects. Currently consists of abstract DAO implementation
  * **sesam** - application framework indenpendend of GWT framework, mostly used for interfaces, domain objects definition and dependency management

  * **releases-repository** - maven repository for acris official releases
  * **snapshot-repository** - maven repository for acris snapshot releases (snapshots are keep up to date after each commit with tested functionality). Be careful when using shapshot repository, because it can be changed anytime.

  * **trunk** - current working version of the Acris
  * **tags** - some special tagged version
  * **branches** - previous version of acris framework

  * **wiki** - holy bible for acris ;)

  1. Download sources from [/trunk](http://acris.googlecode.com/svn/trunk/)
  1. Create eclipse projects using maven eclipse plugin: mvn eclipse:clean eclipse:eclipse
  1. Import projects into the eclipse File -> Import -> ...

# Contributors #

Just ask for contribution access to the SVN repository :)