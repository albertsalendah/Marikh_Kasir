<?php

class MethodPembayaranOperation{
	
	private $con; 

        function __construct(){
            require_once dirname(__FILE__) . '/DbConnect.php';
            $db = new DbConnect; 
            $this->con = $db->connect(); 
        }
		
		public function ListMethodPembayaran(){
            $stmt = $this->con->prepare("SELECT id_method_bayar,nama_method,reguler,olfood FROM tb_method_bayar;");
            $stmt->execute();
            $stmt->bind_result($id_method_bayar,$nama_method,$reguler,$olfood);
            $ArrayMethodBayar = array(); 
            while($stmt->fetch()){ 
                $MethodBayar = array(); 
				$MethodBayar['id_method_bayar'] = $id_method_bayar; 
                $MethodBayar['nama_method'] = $nama_method; 
				$MethodBayar['reguler'] = $reguler;  
				$MethodBayar['olfood'] = $olfood;  
                array_push($ArrayMethodBayar, $MethodBayar);
            }             
            return $ArrayMethodBayar; 
        }
		
		public function tambahMethodBayar($nama_method,$reguler,$olfood){
			if(!$this->cekMethodbayar($nama_method)){	
			$stmt = $this->con->prepare("INSERT INTO tb_method_bayar (nama_method,reguler,olfood) VALUES (?,?,?)");
			$stmt->bind_param("sss",$nama_method,$reguler,$olfood);
			if($stmt->execute()){
				return true; 
			}else{
				return false;
			}
			}else{
				return false;
			}
		}
		
		private function cekMethodbayar($nama_method){
            $stmt = $this->con->prepare("SELECT nama_method FROM tb_method_bayar WHERE nama_method = ?");
            $stmt->bind_param("s", $nama_method);
            $stmt->execute(); 
            $stmt->store_result(); 
            return $stmt->num_rows > 0;  
        }
		
		public function updateMethodBayar($id_method_bayar,$nama_method,$reguler,$olfood){
				$stmt = $this->con->prepare("UPDATE tb_method_bayar SET nama_method = ?,reguler = ?,olfood = ? WHERE id_method_bayar = ?");
				$stmt->bind_param("ssss",$nama_method,$reguler,$olfood,$id_method_bayar);
				if($stmt->execute())
					return true; 
				return false;			
        }
		
		public function getMethodBayarByID($id_method_bayar){
            $stmt = $this->con->prepare("SELECT id_method_bayar,nama_method,reguler,olfood FROM tb_method_bayar WHERE id_method_bayar = ?");
            $stmt->bind_param("s", $id_method_bayar);
            $stmt->execute(); 
            $stmt->bind_result($id_method_bayar,$nama_method,$reguler,$olfood);
            $stmt->fetch(); 
            $MethodBayar = array(); 
				$MethodBayar['id_method_bayar'] = $id_method_bayar; 
                $MethodBayar['nama_method'] = $nama_method; 
				$MethodBayar['reguler'] = $reguler;  
				$MethodBayar['olfood'] = $olfood;  
            return $MethodBayar; 
        }
		
}