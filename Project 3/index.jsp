 
<!doctype html>
<%-- 
    
/*  Name: Showad Huda
	Course: CNT 4714 – Summer 2021 – Project Three
	Assignment title: A Three-Tier Distributed Web-Based Application
	Date: August 5, 2021
*/
   
--%>

<%
    String inputBox = (String) session.getAttribute("inputBox");
    String outputs = (String) session.getAttribute("outputs");
    if(outputs == null){
        outputs = " ";
   }
   if(inputBox == null){
       inputBox = " ";
   }
%>
    
<html lang="en">
<head>
    <!-- Meta set tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <!-- Bootstrap CND style -->
	<!-- Latest compiled and minified CSS -->
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">
    <title>Project 3 A Three-Tier Distributed Web-Based Application</title>
	<!-- Reset to clean up after extension and clear form -->
    <script src="reset.js"></script>  
</head>

<body>
    <div class="container-fluid ">
        <row class="row justify-content-center">
            <h1 class="text-center">Welcome to the Summer 2021 Project 3 Enterprise Database System </h1>
            <h3 class="text-center">A Servlet/JSP-based Multi-tiered Enterprise Application Using A Tomcat Container</div>
            <hr>
			<div class="text-center">You are connected to the Project 3 Enterprise System database as a root user.</div>
            <div class="text-center">Please enter any valid SQL query or update command.</div>
            <form action = "/project3/SQLServlet" method = "post" style="margin-top: 15px;" class="text-center">
                <div class="form-group row">
                    <div class=" col-sm-12 col-md-12 col-lg-12">
                        <textarea name="inputBox" class="form-control" id="inputBox" rows="8" cols="50"><%= inputBox %></textarea>
                    </div>
                </div>

                <button style="margin-bottom: 15px;" type="submit" class="btn btn-dark">Execute Command</button>
                <button onClick="reset();" style="margin-bottom: 15px;" type="reset" class="btn btn-dark">Clear Form</button>
            <div class="text-center">All execution results will appear below this line.</div>
			<hr>
			</form>
        </row>
    </div>

    <div class="text-center">
        <%-- centered style formatting with output--%>
        <%= outputs %>
    </div>
    </div>
</body>

</html>