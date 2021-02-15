<?php
$conn=new mysqli('localhost','root','','E-doctor');
if($conn->connect_error) echo mysqli_connect_error();
else{
    if($_POST['operation']=="insert"){
        $statement2=$conn->prepare("SELECT * FROM Users WHERE `Username` = ?");
        $statement2->bind_param('s',$user);
        $user=$_POST['username'];
        $statement2->execute();
        $statement2->store_result();
        if($statement2->num_rows() == 0){
            $statement=$conn->prepare("INSERT INTO Users(`Username`,`Password`) VALUES(?,?)");
            $statement->bind_param('ss',$username,$password); /// ss for string string the values types
            $username=$_POST['username'];
            $password=$_POST['password'];
            if($statement->execute()) echo 'Account created';
            else echo 'Account not created !';
            $statement->close();
        }
        else{
            echo 'the username is already in use';
        }
        $statement2->close();
    }
    else if($_POST['operation']=="login"){
        $statement=$conn->prepare("SELECT * FROM Users WHERE `Username` LIKE ? AND `Password` LIKE ?");
        $statement->bind_param('ss',$username,$password);
        
        $username=$_POST['username'];
        $password=$_POST['password'];
        
        $statement->execute();
        $statement->store_result();
        if($statement->num_rows() != 0) echo 'success';
        else echo 'incorrect input';
        
        $statement->close();
    }
    else if($_POST['operation']=="qsts"){
        $statement=$conn->prepare("SELECT * FROM nodes WHERE `Id` LIKE ?");
        $statement->bind_param('i',$id);
        
        $id=intval($_POST['id']);
        
        $statement->execute();
        $statement->store_result();
        $statement->bind_result($id,$content,$default,$yes,$no);
        $statement->fetch();
        $data=array("content"=>$content,"yes"=>$yes,"no"=>$no);
        echo json_encode($data);
        
        $statement->close();
    }
}
