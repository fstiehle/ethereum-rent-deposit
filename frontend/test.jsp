<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<script src="Resource/jquery/jquery.min.js"></script>
<script src="Resource/bootstrap/js/bootstrap.js"></script>
<title>registration page</title>
<link href="Resource/bootstrap/css/bootstrap.css" rel="stylesheet" />
<link href="Resource/css/taobao.css" rel="stylesheet" />
</head>
	
<body>
	
	<!--  start -->
	<div class="container">
		<div class="row info-content">
			<form id="registerForm" class="form-horizontal" method="post"
				action="" style="margin-top: 20px;">

				<div class="form-group">
					<label class="col-sm-2 control-label">First name</label>
					<div class="col-sm-3">
						<input type="text" id="Firstname" value="" name="FirstName"
							class="form-control" placeholder="Please enter your first name">
					</div>

				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">Last name</label>
					<div class="col-sm-3">
						<input type="text" id="Lastname" value="" name="LastName"
							class="form-control" placeholder="Please enter your last name">
					</div>

				</div>
				
				
				<div class="form-group">
				<label class="col-sm-2 control-label">Birthday</label>
				<div class="col-sm-3">
						<input type="date" id="birthday" value="" name="Birthday"
							class="form-control" placeholder="DD-MM-YYYY">
				</div>
					
				</div>
				

				<div class="form-group">
					<label class="col-sm-2 control-label">E-Mail</label>
					<div class="col-sm-3">
						<input type="text" id="email" value="" name="Email"
							class="form-control" placeholder="Please enter your email">
					</div>
					<div class="col-sm-3">
						<span style="color: red;"></span>
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-sm-2 control-label">Street</label>
					<div class="col-sm-3">
						<input type="text" id="street" value="" name="Street"
							class="form-control" placeholder="Please enter your street">
					</div>
					
					<label class="col-sm-2 control-label">PLZ</label>
					<div class="col-sm-3">
						<input type="text" id="plz" value="" name="PLZ"
							class="form-control" placeholder="Please enter your PLZ">
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-sm-2 control-label">House Number</label>
					<div class="col-sm-3">
						<input type="text" id="houseNumber" value="" name="HouseNumber"
							class="form-control" placeholder="Please enter your houseNumber">
					</div>
					
					<label class="col-sm-2 control-label">City</label>
					<div class="col-sm-3">
						<input type="text" id="city" value="" name="City"
							class="form-control" placeholder="Please enter your city">
					</div>
				</div>
				
				
				
				<div class="form-group">
				
					<label class="col-sm-2 control-label">Land</label>
					<div class="col-sm-3">
						<input type="text" id="land" value="" name="Land"
							class="form-control" placeholder="Please enter your Land">
					</div>
					
					<label class="col-sm-2 control-label">Country</label>
					<div class="col-sm-3">
						<input type="text" id="country" value="" name="Country"
							class="form-control" placeholder="Please enter your country">
					</div>
					
					
				</div>
				
				
				<div class="form-group">
					<label class="col-sm-2 control-label"></label>
					<div class="col-sm-3">
						<button type="submit" id="btn_submit" class="btn btn-success">
							<span class="glyphicon glyphicon-user">Register
						</button>
						<button type="reset" class="btn btn-info">
							<span class="glyphicon glyphicon-edit">Reset
						</button>
					</div>
				</div>
			</form>
		</div>
		<footer>
			<p>&copy; <b>Copyright 2020</b></p>
		</footer>
	
	<script>
	$("#btn_submit").click(function(){
            var username = document.getElementById("Firstname").value;
            var password = document.getElementById("Lastname").value;
            var username = document.getElementById("birthday").value;
            var password = document.getElementById("email").value;
            var username = document.getElementById("street").value;
            var password = document.getElementById("plz").value;
            var username = document.getElementById("houseNumber").value;
            var password = document.getElementById("city").value;
            var username = document.getElementById("land").value;
            var password = document.getElementById("country").value;
            
            $.ajax({
            	url: '/user/login',
                type: "POST",
                dataType: "json",
                contentType: "application/json;charset=utf-8",
                data:JSON.stringify({
                    "FirstName": Firstname,
                    "LastName": Lastname,
                    "Birthday": birthday,
                    "Email": email,
                    "Street": street,
                    "PLZ": plz,
                    "HouseNumber": houseNumber,
                    "City": city,
                    "Land": land,
                    "Country": country
                    	  	    
                }),
                success: function (result) {
                    console.log("data is :" + result)
                    if (result.code == 200) {
                        alert("Successfully saved");
                        window.location.href = "../home/home_page.html";
                    }else {
                        alert(result.message)
                    }
                }
            });
	});
    </script>

	
		
	</div>
	<!--end  -->
</body>
</html>

 