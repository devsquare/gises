<%@include file="interpreter.jsp"%><%
    String command = request.getParameter("cmd");
    //parse the command
    //get actions
    //return actions
    String action = understandCommand(command);
//    String action = "{\"action\":\"display\",  \"params\": { \"lat\": \"38.8833\", \"long\":\"-77.0167\"} }";
    out.print(action);
%>