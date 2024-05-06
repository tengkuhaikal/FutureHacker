<?php
$eventname = $_POST['eventname'];
$description = $_POST['description'];
$date = $_POST['date'];

if(!empty($eventname) || !empty($description) || !empty($date)) {
    $host = "localhost";
    $dbEventname = "root";
    $dbDescription = "";
    $dbName = "events";

    $connection = new mysqli ($host,$dbEventname,$dbDescription,$dbName);

    if(mysqli_connect_error) {
        
    }
} else {
    echo "All field are required";
    die();
}

?>