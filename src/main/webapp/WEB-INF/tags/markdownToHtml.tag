<%@ tag body-content="empty" import="com.github.rjeschke.txtmark.Processor" %>
<%@ attribute name="markdown" type="java.lang.String" required="true" %>
<%= Processor.process(markdown) %>
