FROM tomcat:8-jre8
MAINTAINER hong.lin<hong.lin@beecloud.com>
ADD beecloud /usr/local/tomcat/webapps/beecloud
ADD ./properties/jdbc.properties /usr/local/tomcat/webapps/beecloud/WEB-INF/classes/jdbc.properties
ADD ./properties/mqtt.properties /usr/local/tomcat/webapps/beecloud/WEB-INF/classes/mqtt.properties
ADD ./properties/tbox.jsp /usr/local/tomcat/webapps/beecloud/WEB-INF/view/bootstrap/tbox.jsp
EXPOSE 8080
