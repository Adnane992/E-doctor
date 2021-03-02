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
        $statement=$conn->prepare("SELECT * FROM Users WHERE `Username` = ? AND `Password` = ?");
        $statement->bind_param('ss',$username,$password);
        
        $username=$_POST['username'];
        $password=$_POST['password'];
        
        $statement->execute();
        $statement->store_result();
        if($statement->num_rows() != 0){
            $statement->bind_result($id,$un,$pass);
            $statement->fetch();
            session_start();
            $session['UserId'] = $id;
            $session = session_id();
            $sql = $conn->prepare("INSERT INTO Sess(`id`,`UserId`,`Username`) VALUES(?,?,?)");
            $sql->bind_param('sis',$session,$id,$username);
            if($sql->execute()) echo 'success@'.strval($id).'@'.session_id();
            else echo 'problem faced establishing session';
            session_destroy();
        }
        else echo 'incorrect input';
        
        $statement->close();
        $conn->close();
    }
    else if($_POST['operation']=="SessionIdVerification"){
        $statement=$conn->prepare("SELECT * FROM Sess WHERE `id` = ?");
        $statement->bind_param('s',$_POST['SessionId']);
        if($statement->execute()){
            $statement->bind_result($id,$userid,$username);
            if($statement->fetch() != null) echo strval($userid).'@'.$username;
            else echo 'null';
        }
        else echo 'false';
    }
    else if($_POST['operation']=="Logout"){
        $session = $_POST['SessionId'];
        $sql = $conn->prepare("DELETE FROM Sess WHERE `id` = ?");
        $sql->bind_param('s',$session);
        if($sql->execute()) echo 'Logout successful';
        else echo 'Logout failed';
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
    else if($_POST['operation']=="setReminder"){
        $statement=$conn->prepare("INSERT INTO Medicines(`User`,`Name`,`Hours`,`Minutes`,`Dosage`,`Status`) VALUES(?,?,?,?,?,?)");
        $statement->bind_param('isiidi',$user,$name,$hours,$minutes,$dosage,$status);
        $user=intval($_POST['User']);
        $name=$_POST['Name'];
        $hours=intval($_POST['Hours']);
        $minutes=intval($_POST['Minutes']);
        $dosage=floatval($_POST['Dosage']);
        $status=0;
        if($statement->execute()) echo "medicine inserted";
        else echo 'error while inserting medicine';
    }
    else if($_POST['operation']=="fetchMeds"){
        $user=intval($_POST['User']);
        $sql="SELECT * FROM Medicines WHERE Medicines.User = $user";
        $result=$conn->query($sql);
        if(mysqli_num_rows($result) != 0){
            echo mysqli_num_rows($result).'@';
            while($row=$result->fetch_row()) echo $row[0].'@';
            mysqli_data_seek($result,0);
            while($row=$result->fetch_row()) echo $row[2].'@';
            mysqli_data_seek($result,0);
            while($row=$result->fetch_row()) echo $row[3].'@';
            mysqli_data_seek($result,0);
            while($row=$result->fetch_row()) echo $row[4].'@';
            mysqli_data_seek($result,0);
            while($row=$result->fetch_row()) echo $row[5].'@';
            mysqli_data_seek($result,0);
            while($row=$result->fetch_row()) echo $row[6].'@';
        }
        else echo 'empty';
        $conn->close();
    }
    else if($_POST['operation']=="updateMed"){
        $name=$_POST['Name'];
        $hours=intval($_POST['Hours']);
        $minutes=intval($_POST['Minutes']);
        $dosage=floatval($_POST['Dosage']);
        $id=intval($_POST['Id']);

        $statement=$conn->prepare("UPDATE Medicines SET Medicines.Name=?, Medicines.Hours=?, Medicines.Minutes=?, Medicines.Dosage=? WHERE Medicines.Id=?");
        $statement->bind_param('siidi',$name,$hours,$minutes,$dosage,$id);
        if($statement->execute()) echo 'Item updated';
        else echo 'Could not update item';
        $statement->close();
        $conn->close();
    }
    else if($_POST['operation']=="delReminder"){
        $id=$_POST['Id'];
        $sql="DELETE FROM Medicines WHERE `Id`=$id";
        if($conn->query($sql)) echo "Reminder removed";
        else "Could not remove this reminder";
    }
    else if($_POST['operation']=="updateStatus"){
        $id=intval($_POST['Id']);
        $status=intval($_POST['Status']);
        $sql="UPDATE Medicines SET Medicines.Status=$status WHERE `Id`=$id";
        if($conn->query($sql)) echo "Status updated";
        else "Status not updated";
    }
}
