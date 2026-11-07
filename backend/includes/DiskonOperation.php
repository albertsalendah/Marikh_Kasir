<?php

class DiskonOperation{
	
	private $con; 

        function __construct(){
            require_once dirname(__FILE__) . '/DbConnect.php';
            $db = new DbConnect; 
            $this->con = $db->connect(); 
        }
		
		public function ListDiskon(){
            $stmt = $this->con->prepare("SELECT id_disc,deskripsi_disc,nominal_disc FROM tb_discount;");
            $stmt->execute();
            $stmt->bind_result($id_disc,$deskripsi_disc,$nominal_disc);
            $ArrayDiskon = array(); 
            while($stmt->fetch()){ 
                $Diskon = array(); 
				$Diskon['id_disc'] = $id_disc; 
				$Diskon['deskripsi_disc'] = $deskripsi_disc; 
                $Diskon['nominal_disc'] = $nominal_disc; 
                array_push($ArrayDiskon, $Diskon);
            }             
            return $ArrayDiskon; 
        }
		
		public function tambahDiskon($deskripsi_disc,$nominal_disc){
			if(!$this->cekDiskon($deskripsi_disc)){	
			$stmt = $this->con->prepare("INSERT INTO tb_discount (deskripsi_disc,nominal_disc) VALUES (?,?)");
			$stmt->bind_param("ss",$deskripsi_disc,$nominal_disc);
			if($stmt->execute()){
				return true; 
			}else{
				return false;
			}
			}else{
				return false;
			}
		}
		
		private function cekDiskon($deskripsi_disc){
            $stmt = $this->con->prepare("SELECT deskripsi_disc FROM tb_discount WHERE deskripsi_disc = ?");
            $stmt->bind_param("s", $deskripsi_disc);
            $stmt->execute(); 
            $stmt->store_result(); 
            return $stmt->num_rows > 0;  
        }
		
		public function updateDiskon($id_disc,$deskripsi_disc,$nominal_disc){
				$stmt = $this->con->prepare("UPDATE tb_discount SET deskripsi_disc = ?,nominal_disc = ? WHERE id_disc = ?");
				$stmt->bind_param("sss",$deskripsi_disc,$nominal_disc,$id_disc);
				if($stmt->execute())
					return true; 
				return false;			
        }
		
		public function getDiskonByID($id_disc){
            $stmt = $this->con->prepare("SELECT id_disc,deskripsi_disc,nominal_disc FROM tb_discount WHERE id_disc = ?");
            $stmt->bind_param("s", $id_disc);
            $stmt->execute(); 
            $stmt->bind_result($id_disc,$deskripsi_disc,$nominal_disc);
            $stmt->fetch(); 
            $Diskon = array(); 
				$Diskon['id_disc'] = $id_disc; 
				$Diskon['deskripsi_disc'] = $deskripsi_disc; 
                $Diskon['nominal_disc'] = $nominal_disc;                 				
            return $Diskon; 
        }
		
}