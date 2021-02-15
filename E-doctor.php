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
        $conn->close();
    }
    else if($_POST['operation']=="login"){
        $statement=$conn->prepare("SELECT * FROM Users WHERE `Username` LIKE ? AND `Password` LIKE ?");
        $statement->bind_param('ss',$username,$password);
        
        $username=$_POST['username'];
        $password=$_POST['password'];
        
        $statement->execute();
        $statement->store_result();
        if($statement->num_rows() != 0){
            $statement->bind_result($id,$un,$pass);
            $statement->fetch();
            echo 'success@'.strval($id);
        }
        else echo 'incorrect input';
        
        $statement->close();
        $conn->close();
    }
    else if($_POST['operation']=="qsts"){
        $id=intval($_POST['id']);
        $sql="SELECT * FROM Nodes WHERE `Id` LIKE $id";
        $result=$conn->query($sql);
        if(mysqli_num_rows($result) != 0){
            $row=$result->fetch_row();
            for($i=1;$i<5;$i++) if(empty($row[$i])) $row[$i]="null";
            $data=array($row[1],$row[2],$row[3],$row[4]);
            echo implode("@",$data);
        }
        else echo 'empty';
        $conn->close();
    }
    else if($_POST['operation']=="fetch_diseases"){
        $sql="SELECT * FROM Diseases";
        $result=$conn->query($sql);
        if(mysqli_num_rows($result) != 0){
            echo mysqli_num_rows($result).'@';
            while($row=$result->fetch_row()) echo $row[0].'@';
            mysqli_data_seek($result,0);
            while($row=$result->fetch_row()) echo $row[1].'@';
            mysqli_data_seek($result,0);
            while($row=$result->fetch_row()) echo $row[2].'@';
        }
        else echo 'empty';
        $conn->close();
    }
    else if($_POST['operation']=="addToHistory"){
        $sql=$conn->prepare("INSERT INTO DiseaseHistory(`User`,`Disease`,`Timestamp`) VALUES(?,?,?)");
        $sql->bind_param('iis',$user,$disease,$timestamp);
        $user=intval($_POST['User']);
        $disease=intval($_POST['Disease']);
        $timestamp=$_POST['Timestamp'];
        if($sql->execute()) echo "history updated";
        else "insertion error !";
        $sql->close();
        $conn->close();
    }
    else if($_POST['operation']=="fetch_history"){
        $user=$_POST['User'];
        $sql="SELECT Diseases.Name,DiseaseHistory.Timestamp FROM DiseaseHistory
             LEFT JOIN Diseases ON DiseaseHistory.Disease = Diseases.Id WHERE DiseaseHistory.User LIKE $user";
        $result=$conn->query($sql);
        if(mysqli_num_rows($result) != 0){
            echo mysqli_num_rows($result).'@';
            while($row=$result->fetch_row()) echo $row[0].'@';
            mysqli_data_seek($result,0);
            while($row=$result->fetch_row()) echo $row[1].'@';
        }
        else echo 'empty';
        $conn->close();
    }

    
}
