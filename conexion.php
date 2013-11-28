<?php
	$host = "mysql13.000webhost.com";
	$username = "a3690059_root";
	$password = "123abc";
	$db_name = "a3690059_appos";
	
	$con = mysql_connect("$host", "$username", "$password") or die ("cannot connect");
	mysql_select_db("$db_name") or die ("cannot select DB");
	$sql = "SELECT * FROM Categoria";
	$result = mysql_query("$sql");
	$json = array();
	
	if(mysql_num_rows($result)){
		while($row = mysql_fetch_assoc($result)){
			$json['categoria'][] = $row;
		}
	}
	
	mysql_close($con);
	echo json_encode($json);
?>
