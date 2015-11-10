# Important milestones in our life #

| [TBD](http://code.google.com/p/acris/issues/list?q=label%3AMilestone-NotSpecified) | TBD        | Issues incubator |
|:-----------------------------------------------------------------------------------|:-----------|:-----------------|
| [2.1.0](http://code.google.com/p/acris/issues/list?can=1&q=label%3AMilestone-2.1.0) | in dev        |                  |
| [2.0.0](http://code.google.com/p/acris/issues/list?can=1&q=label%3AMilestone-2.0.0) | 26.9.2014        |  Force everywhere release |
| [1.2.2](http://code.google.com/p/acris/issues/list?can=1&q=label%3AMilestone-1.2.2) | 24.12.2012        |  Christmas Day release |
| [1.2.1](http://code.google.com/p/acris/issues/list?can=1&q=label%3AMilestone-1.2.1) | 25.05.2012        |  Towel Day release |
| [1.2.0](http://code.google.com/p/acris/issues/list?can=1&q=label%3AMilestone-1.2.0) | 08.10.2011        |  Kuvikajúci Kanec Kľačí release |
| [1.1.2](http://code.google.com/p/acris/issues/list?can=1&q=label%3AMilestone-1.1.2) | 16.06.2011        |  Malý Macík Mrnčí release |
| [1.1.1](http://code.google.com/p/acris/issues/list?can=1&q=label%3AMilestone-1.1.1) | 13.05.2011        |  Hacked Hladny Hadik |
| [1.1.0](http://code.google.com/p/acris/issues/list?can=1&q=label%3AMilestone-1.1.0) | 02.12.2010        |  Hladný Hadík release |
| [1.0.1](http://code.google.com/p/acris/issues/list?can=1&q=label%3AMilestone-1.0.1) | 04.10.2010 | Bugfixing + Security generalization |
| 1.0.0                                                                              | 20.07.2010 | Release          |
| 0.9.0                                                                              | 01.07.2010 | Release Candidate 1 |

# Where to start #

There are prepared Maven parent projects for you.

Read **ProjectQuickStart**

# Organization of the development #

All bugs, improvements and feature requests are handled by **issue tracker**. You can always click on the link to the release to see the list of issues.

**Use appropriate template** when submitting an issue.

# Release 2.0.0 #

  * SVN to Git migration

## Features ##

Sesam:
  * sesam-jndi independent of other 3rd packages
  * processor fixes
  * equals and hashcode can be changed using @Key annotation

Corpis:
  * Payment module documentation
  * TrustPay and HomeCredit integration
  * handling Hibernate Javassist proxies
  * processor fixes

Acris:
  * GWT 2.6 migration
  * Player and Recorder for activity logging on the page
  * ACL computation speedup

# [Older release notes](OldReleaseNotes.md) #