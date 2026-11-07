<?php 

    class UserOperation{

        private $con; 

        function __construct(){
            require_once dirname(__FILE__) . '/DbConnect.php';
            $db = new DbConnect; 
            $this->con = $db->connect(); 
        }
		
		public function userLogin($username, $password){
            if($this->isUsernameExist($username)){
                $hashed_password = $this->getUsersPasswordByUsername($username); 
                if($password == $hashed_password){
                    return USER_AUTHENTICATED;
                }else{
                    return USER_PASSWORD_DO_NOT_MATCH; 
                }
            }else{
                return USER_NOT_FOUND; 
            }
        }

        private function getUsersPasswordByUsername($username){
            $stmt = $this->con->prepare("SELECT password FROM tb_user WHERE username = ?");
            $stmt->bind_param("s", $username);
            $stmt->execute(); 
            $stmt->bind_result($password);
            $stmt->fetch(); 
            return $password; 
        }
		
		private function isUsernameExist($username){
            $stmt = $this->con->prepare("SELECT username FROM tb_user WHERE username = ?");
            $stmt->bind_param("s", $username);
            $stmt->execute(); 
            $stmt->store_result(); 
            return $stmt->num_rows > 0;  
        }
		
		public function getUserByUsername($username){
            $stmt = $this->con->prepare("SELECT id_user, username, hp_user,tgl_lahir_user,jenis_user FROM tb_user WHERE username = ?");
            $stmt->bind_param("s", $username);
            $stmt->execute(); 
            $stmt->bind_result($id_user, $username,$hp_user,$tgl_lahir_user,$jenis_user);
            $stmt->fetch(); 
            $user = array(); 
            $user['id_user'] = $id_user; 
            $user['username']=$username; 
			$user['hp_user']=$hp_user;
			$user['tgl_lahir_user']=$tgl_lahir_user;
			$user['jenis_user']=$jenis_user;
            return $user; 
        }
		
	}