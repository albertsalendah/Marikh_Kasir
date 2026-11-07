<?php

class PelangganOperation{
	
	private $con; 

        function __construct(){
            require_once dirname(__FILE__) . '/DbConnect.php';
            $db = new DbConnect; 
            $this->con = $db->connect(); 
        }
        
        function generateIdPelanggan(){
		$stmt = $this->con->prepare("SELECT COUNT(*) FROM tb_pelanggan;");
		$stmt->execute(); 
		$stmt->bind_result($num);
		$stmt->fetch();
		$ID = sprintf('%04d',$num+1);			
		$id_pelanggan = "PL{$ID}";
		return $id_pelanggan;
	}
		
		public function tambahPelanggan($nama_pelanggan,$hp_pelanggan,$email_pelanggan,$tgl_lahir_pelanggan,$id_kat_pelanggan){
		    $id_pelanggan = $this->generateIdPelanggan();
			if(!$this->ceknamapelanggan($nama_pelanggan)){	
			$stmt = $this->con->prepare("INSERT INTO tb_pelanggan (id_pelanggan,nama_pelanggan,hp_pelanggan,email_pelanggan,tgl_lahir_pelanggan,id_kat_pelanggan) VALUES (?,?,?,?,?,?)");
			$stmt->bind_param("ssssss",$id_pelanggan,$nama_pelanggan,$hp_pelanggan,$email_pelanggan,$tgl_lahir_pelanggan,$id_kat_pelanggan);
			if($stmt->execute()){
				return true; 
			}else{
				return false;
			}
			
			}else{
				return false;
			}
		}
		
		private function ceknamapelanggan($nama_pelanggan){
            $stmt = $this->con->prepare("SELECT nama_pelanggan FROM tb_pelanggan WHERE nama_pelanggan = ?");
            $stmt->bind_param("s", $nama_pelanggan);
            $stmt->execute(); 
            $stmt->store_result(); 
            return $stmt->num_rows > 0;  
        }
		
		public function updatePelanggan($id_pelanggan,$nama_pelanggan,$hp_pelanggan,$email_pelanggan,$tgl_lahir_pelanggan,$id_kat_pelanggan){
				$stmt = $this->con->prepare("UPDATE tb_pelanggan SET nama_pelanggan = ?,hp_pelanggan = ?,email_pelanggan = ?,tgl_lahir_pelanggan = ?,id_kat_pelanggan = ? WHERE id_pelanggan = ?");
				$stmt->bind_param("ssssss",$nama_pelanggan,$hp_pelanggan,$email_pelanggan,$tgl_lahir_pelanggan,$id_kat_pelanggan,$id_pelanggan);
				if($stmt->execute())
					return true; 
				return false;			
        }
		
		public function hapusPelanggan($id_pelanggan){
            $stmt = $this->con->prepare("DELETE FROM tb_pelanggan WHERE id_pelanggan = ?");
            $stmt->bind_param("s", $id_pelanggan);
            if($stmt->execute())
                return true; 
            return false; 
        }
		
		public function getPelangganByID($id_pelanggan){
            $stmt = $this->con->prepare("SELECT id_pelanggan,nama_pelanggan,hp_pelanggan,email_pelanggan,tgl_lahir_pelanggan,id_kat_pelanggan FROM tb_pelanggan WHERE id_pelanggan = ?");
            $stmt->bind_param("s", $id_pelanggan);
            $stmt->execute(); 
            $stmt->bind_result($id_pelanggan,$nama_pelanggan,$hp_pelanggan,$email_pelanggan,$tgl_lahir_pelanggan,$id_kat_pelanggan);
            $stmt->fetch(); 
            $Pelanggan = array(); 
				$Pelanggan['id_pelanggan'] = $id_pelanggan; 
                $Pelanggan['nama_pelanggan'] = $nama_pelanggan; 
                $Pelanggan['hp_pelanggan'] = $hp_pelanggan; 
                $Pelanggan['email_pelanggan'] = $email_pelanggan; 
				$Pelanggan['tgl_lahir_pelanggan'] = $tgl_lahir_pelanggan;
				$Pelanggan['id_kat_pelanggan'] = $id_kat_pelanggan;
				
            return $Pelanggan; 
        }
		
		public function ListPelanggan(){
            $stmt = $this->con->prepare("SELECT id_pelanggan,nama_pelanggan,hp_pelanggan,email_pelanggan,tgl_lahir_pelanggan,id_kat_pelanggan FROM tb_pelanggan;");
            $stmt->execute();
            $stmt->bind_result($id_pelanggan,$nama_pelanggan,$hp_pelanggan,$email_pelanggan,$tgl_lahir_pelanggan,$id_kat_pelanggan);
            $ArrayPelanggan = array(); 
            while($stmt->fetch()){ 
                $Pelanggan = array(); 
				$Pelanggan['id_pelanggan'] = $id_pelanggan; 
                $Pelanggan['nama_pelanggan'] = $nama_pelanggan; 
                $Pelanggan['hp_pelanggan'] = $hp_pelanggan; 
                $Pelanggan['email_pelanggan'] = $email_pelanggan; 
				$Pelanggan['tgl_lahir_pelanggan'] = $tgl_lahir_pelanggan;
				$Pelanggan['id_kat_pelanggan'] = $id_kat_pelanggan;
                array_push($ArrayPelanggan, $Pelanggan);
            }             
            return $ArrayPelanggan; 
        }
		
		public function ListKategoriPelanggan(){
            $stmt = $this->con->prepare("SELECT id_kat_pelanggan,nama_kat_pelanggan FROM tb_kat_pelanggan;");
            $stmt->execute();
            $stmt->bind_result($id_kat_pelanggan,$nama_kat_pelanggan);
            $ArrayKategoriPelanggan = array(); 
            while($stmt->fetch()){ 
                $KategoriPelanggan = array(); 
				$KategoriPelanggan['id_kat_pelanggan'] = $id_kat_pelanggan; 
                $KategoriPelanggan['nama_kat_pelanggan'] = $nama_kat_pelanggan; 
                array_push($ArrayKategoriPelanggan, $KategoriPelanggan);
            }             
            return $ArrayKategoriPelanggan; 
        }
		
		public function tambahKategoriPelanggan($nama_kat_pelanggan){
			if(!$this->ceknamakategoripelanggan($nama_kat_pelanggan)){	
			$stmt = $this->con->prepare("INSERT INTO tb_kat_pelanggan (nama_kat_pelanggan) VALUES (?)");
			$stmt->bind_param("s",$nama_kat_pelanggan);
			if($stmt->execute()){
				return true; 
			}else{
				return false;
			}
			}else{
				return false;
			}
		}
		
		public function updateKategoriPelanggan($id_kat_pelanggan,$nama_kat_pelanggan){
				$stmt = $this->con->prepare("UPDATE tb_kat_pelanggan SET nama_kat_pelanggan = ? WHERE id_kat_pelanggan = ?");
				$stmt->bind_param("ss",$nama_kat_pelanggan,$id_kat_pelanggan);
				if($stmt->execute())
					return true; 
				return false;			
        }
		
		public function getKategoriPelangganByID($id_kat_pelanggan){
            $stmt = $this->con->prepare("SELECT id_kat_pelanggan,nama_kat_pelanggan FROM tb_kat_pelanggan WHERE id_kat_pelanggan = ?");
            $stmt->bind_param("s", $id_kat_pelanggan);
            $stmt->execute(); 
            $stmt->bind_result($id_kat_pelanggan,$nama_kat_pelanggan);
            $stmt->fetch(); 
            $KategoriPelanggan = array(); 
				$KategoriPelanggan['id_kat_pelanggan'] = $id_kat_pelanggan; 
                $KategoriPelanggan['nama_kat_pelanggan'] = $nama_kat_pelanggan;                 				
            return $KategoriPelanggan; 
        }
		
		public function hapusKategoriPelanggan($id_kat_pelanggan){
            $stmt = $this->con->prepare("DELETE FROM tb_kat_pelanggan WHERE id_kat_pelanggan = ?");
            $stmt->bind_param("s", $id_kat_pelanggan);
            if($stmt->execute())
                return true; 
            return false; 
        }
		
		private function ceknamakategoripelanggan($nama_kat_pelanggan){
            $stmt = $this->con->prepare("SELECT nama_kat_pelanggan FROM tb_kat_pelanggan WHERE nama_kat_pelanggan = ?");
            $stmt->bind_param("s", $nama_kat_pelanggan);
            $stmt->execute(); 
            $stmt->store_result(); 
            return $stmt->num_rows > 0;  
        }
		
}