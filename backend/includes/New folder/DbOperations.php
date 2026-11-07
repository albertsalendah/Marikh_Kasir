<?php 

    class DbOperations{

        private $con; 

        function __construct(){
            require_once dirname(__FILE__) . '/DbConnect.php';
            $db = new DbConnect; 
            $this->con = $db->connect(); 
        }

        public function createUser($username, $password, $nama_cafe, $alamat,$email){
           if(!$this->isEmailExist($email)){
                $stmt = $this->con->prepare("INSERT INTO datacafe (username,password, nama_cafe, alamat,email) VALUES (?, ?, ?, ?,?)");
                $stmt->bind_param("sssss", $username, $password, $nama_cafe, $alamat,$email);
                if($stmt->execute()){
                    return USER_CREATED; 
                }else{
                    return USER_FAILURE;
                }
           }
           return USER_EXISTS; 
        }

        public function userLogin($email, $password){
            if($this->isEmailExist($email)){
                $hashed_password = $this->getUsersPasswordByEmail($email); 
                if(password_verify($password, $hashed_password)){
                    return USER_AUTHENTICATED;
                }else{
                    return USER_PASSWORD_DO_NOT_MATCH; 
                }
            }else{
                return USER_NOT_FOUND; 
            }
        }

        private function getUsersPasswordByEmail($email){
            $stmt = $this->con->prepare("SELECT password FROM datacafe WHERE email = ?");
            $stmt->bind_param("s", $email);
            $stmt->execute(); 
            $stmt->bind_result($password);
            $stmt->fetch(); 
            return $password; 
        }

        public function getAllUsers(){
            $stmt = $this->con->prepare("SELECT username, nama_cafe, alamat, email FROM datacafe;");
            $stmt->execute(); 
            $stmt->bind_result($username, $nama_cafe, $alamat, $email);
            $users = array(); 
            while($stmt->fetch()){ 
                $user = array(); 
                $user['username'] = $username; 
                $user['nama_cafe']=$nama_cafe; 
                $user['alamat'] = $alamat; 
                $user['email'] = $email; 
                array_push($users, $user);
            }             
            return $users; 
        }

        public function getUserByEmail($email){
            $stmt = $this->con->prepare("SELECT username, nama_cafe, alamat, email FROM datacafe WHERE email = ?");
            $stmt->bind_param("s", $email);
            $stmt->execute(); 
            $stmt->bind_result($username, $nama_cafe, $alamat, $email);
            $stmt->fetch(); 
            $user = array(); 
            $user['username'] = $username; 
            $user['nama_cafe']=$nama_cafe; 
            $user['alamat'] = $alamat; 
            $user['email'] = $email; 
            return $user; 
        }

        public function updateUser($email, $nama_cafe, $alamat, $username){
            $stmt = $this->con->prepare("UPDATE datacafe SET email = ?, nama_cafe = ?, alamat = ? WHERE username = ?");
            $stmt->bind_param("ssss", $email, $nama_cafe, $alamat, $username);
            if($stmt->execute())
                return true; 
            return false; 
        }

        public function updatePassword($currentpassword, $newpassword, $email){
            $hashed_password = $this->getUsersPasswordByEmail($email);
            
            if(password_verify($currentpassword, $hashed_password)){
                
                $hash_password = password_hash($newpassword, PASSWORD_DEFAULT);
                $stmt = $this->con->prepare("UPDATE datacafe SET password = ? WHERE email = ?");
                $stmt->bind_param("ss",$hash_password, $email);

                if($stmt->execute())
                    return PASSWORD_CHANGED;
                return PASSWORD_NOT_CHANGED;

            }else{
                return PASSWORD_DO_NOT_MATCH; 
            }
        }

        public function deleteUser($id){
            $stmt = $this->con->prepare("DELETE FROM datacafe WHERE username = ?");
            $stmt->bind_param("s", $username);
            if($stmt->execute())
                return true; 
            return false; 
        }

        private function isEmailExist($email){
            $stmt = $this->con->prepare("SELECT username FROM datacafe WHERE email = ?");
            $stmt->bind_param("s", $email);
            $stmt->execute(); 
            $stmt->store_result(); 
            return $stmt->num_rows > 0;  
        }
		
		public function createKategori($nama_kategori){
           if(!$this->jikaKategoriAda($nama_kategori)){
                $stmt = $this->con->prepare("INSERT INTO kategori (nama_kategori) VALUES (?)");
                $stmt->bind_param("s", $nama_kategori);
                if($stmt->execute()){
                    return KATEGORI_BERHASIL_DIBUAT; 
                }else{
                    return KATEGORI_GAGAL_DI_DIBUAT;
                }
           }
           return USER_EXISTS; 
        }
		private function jikaKategoriAda($nama_kategori){
            $stmt = $this->con->prepare("SELECT id FROM kategori WHERE nama_kategori = ?");
            $stmt->bind_param("s", $nama_kategori);
            $stmt->execute(); 
            $stmt->store_result(); 
            return $stmt->num_rows > 0;  
        }
		
		public function ListKategori(){
            $stmt = $this->con->prepare("SELECT id, nama_kategori FROM kategori;");
            $stmt->execute(); 
            $stmt->bind_result($id, $nama_kategori);
            $ArrayKategori = array(); 
            while($stmt->fetch()){ 
                $kategori = array(); 
                $kategori['id'] = $id; 
                $kategori['nama_kategori']=$nama_kategori; 
              
                array_push($ArrayKategori, $kategori);
            }             
            return $ArrayKategori; 
        }
		
		public function updateKategori($id, $nama_kategori){
            $stmt = $this->con->prepare("UPDATE kategori SET nama_kategori = ? WHERE id = ?");
            $stmt->bind_param("ss",$nama_kategori, $id);
            if($stmt->execute())
                return true; 
            return false; 
        }
		
		public function getKategoriByID($id){
            $stmt = $this->con->prepare("SELECT nama_kategori FROM kategori WHERE id = ?");
            $stmt->bind_param("s", $id);
            $stmt->execute(); 
            $stmt->bind_result($nama_kategori);
            $stmt->fetch(); 
            $kategori = array(); 
            $kategori['id'] = $id; 
            $kategori['nama_kategori']=$nama_kategori; 
           
            return $kategori; 
        }
    }