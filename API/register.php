<?php
require "init.php";
$name = $_POST["name"];
$email = $_POST["email"];
$user_name = $_POST["user_name"];
$password = $_POST["password"];

$sql = "select * from user_info where email like '.$email'";

$result = mysqli_query($con,$sql);
$response = array();

if(mysqli_num_rows($result)>0){
  $code = "reg_failed";
  $message = "User already exist.....";
  array_push($response,array("code"=>$code,"message"=>$message));
  echo json_encode($response);
}
else{
  $sql = "insert into user_info values('.$name','.$email','.$user_name','.$password')";
  $result = mysqli_query($con,$sql);
  $code = "reg_success";
  $message = "Thank you for register with us.....";
  array_push($response,array("code"=>$code,"message"=>$message));
  echo json_encode($response);
}

mysqli_close($con);
 ?>
