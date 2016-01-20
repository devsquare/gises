<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <meta name="viewport" content="initial-scale=1, maximum-scale=1,user-scalable=no"/>
    <title>Gises</title>
    <style>
        html, body, #map-canvas {
            margin: 0;
            padding: 0;
            height: 100%;
        }
    </style>
    <script src="https://maps.googleapis.com/maps/api/js?v=3.exp"></script>

    <script>
        var map;

        function initialize() {
            var mapOptions = {
                zoom: 9,
                center: new google.maps.LatLng(37.75,-122.45),
                mapTypeId: google.maps.MapTypeId.ROADMAP
            };

            map = new google.maps.Map(document.getElementById('map-canvas'), mapOptions);

        }

        google.maps.event.addDomListener(window, 'load', initialize);

    </script>

    <script>

        function textKeyup(event){
            if(event.keyCode == 13) {
                document.getElementById('go_button').click();
            }
        }

        var xmlhttp;
        function go_handle(event){
            var text_s = document.getElementById('command_text').value;
//            alert(text_s);

            var xmlhttp;
            if (text_s == "") {
                alert("RETURN");
                return;
            }

            if (window.XMLHttpRequest) {// code for IE7+, Firefox, Chrome, Opera, Safari
                xmlhttp = new XMLHttpRequest();
            } else {// code for IE6, IE5
                xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
            }

            xmlhttp.onreadystatechange = function () {
                if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
                    var action_res = xmlhttp.responseText;
                    action_res = action_res.trim();
                    doAction(action_res);
                }
            }
            text_s = encodeURI(text_s);
            xmlhttp.open("GET", "action/handler.jsp?cmd=" + text_s, true);
            xmlhttp.send();

        }

        function doAction(action_res ){
//            alert(action_res);
            var act_obj = JSON.parse(action_res);

//            alert(act_obj);
//            alert(act_obj.action);
//            alert(act_obj.params.lat);
            var lat = act_obj.params.lat;
            var long = act_obj.params.long;

            var goto_point = new google.maps.LatLng( lat,  long) ;
            map.setCenter(goto_point);

        }


    </script>

</head>

<body>

<table width="100%" height="100%">

    <tr height="97%">
        <td width="100%">
            <div id="map-canvas" ></div>
        </td>
    </tr>

    <tr height="3%">
        <td width="100%">
            <form action="javascript: void(0)">
                <input id="command_text" type="text"  style="width: 90%; height: 20px;" onkeyup="textKeyup(event);">
                <input id="go_button" type="button" value="GO" style="width: 5%; height: 20px;" onclick="go_handle();">
            </form>
        </td>
    </tr>
</table>
</body>
</html>
