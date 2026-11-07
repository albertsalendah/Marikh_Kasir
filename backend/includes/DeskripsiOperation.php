<?php
	class DeskripsiOperation{
		function __construct(){
            require_once dirname(__FILE__) . '/DbConnect.php';
            $db = new DbConnect; 
            $this->con = $db->connect(); 
	}
	
	public function tambah_deskripsi_produk($nama_deskripsi,$id_kat_produk){
		$stmt = $this->con->prepare("INSERT INTO tb_deskripsi (nama_deskripsi,id_kat_produk) VALUES (?,?)");
		$stmt->bind_param("ss",$nama_deskripsi,$id_kat_produk);
		if($stmt->execute()){
			return true;
		}else{
		return false;
		}
	}
	
	public function getdaftardeskripsi($id_kat_produk){
		$stmt = $this->con->prepare("SELECT id_deskripsi,nama_deskripsi,id_kat_produk FROM tb_deskripsi WHERE id_kat_produk = ?");
            $stmt->bind_param("s", $id_kat_produk);
			$stmt->execute(); 
            $stmt->bind_result($id_deskripsi,$nama_deskripsi,$id_kat_produk);
            $ArrayDeskripsi = array(); 
            while($stmt->fetch()){ 
                $Deskripsi = array(); 
                $Deskripsi['id_deskripsi']=$id_deskripsi; 
				$Deskripsi['nama_deskripsi']=$nama_deskripsi; 
				$Deskripsi['id_kat_produk']=$id_kat_produk; 			
              
                array_push($ArrayDeskripsi, $Deskripsi);
            }             
            return $ArrayDeskripsi; 
	}
	
	public function getDeskripsiByID($id_deskripsi){
            $stmt = $this->con->prepare("SELECT id_deskripsi,nama_deskripsi,id_kat_produk FROM tb_deskripsi WHERE id_deskripsi = ?");
            $stmt->bind_param("s", $id_deskripsi);
            $stmt->execute(); 
            $stmt->bind_result($id_deskripsi,$nama_deskripsi,$id_kat_produk);
            $stmt->fetch(); 
            $Deskripsi = array(); 
				$Deskripsi['id_deskripsi']=$id_deskripsi; 
				$Deskripsi['nama_deskripsi']=$nama_deskripsi; 
				$Deskripsi['id_kat_produk']=$id_kat_produk; 
							
            return $Deskripsi; 
        }
		
		public function update_deskripsi_produk($id_deskripsi,$nama_deskripsi){
		$stmt = $this->con->prepare("UPDATE tb_deskripsi set nama_deskripsi = ? WHERE id_deskripsi = ?");
		$stmt->bind_param("ss",$nama_deskripsi,$id_deskripsi);
		if($stmt->execute()){
			return true;
		}else{
		return false;
		}
	}
	
	public function hapusdeskripsi($id_deskripsi){
            $stmt = $this->con->prepare("DELETE FROM tb_deskripsi WHERE id_deskripsi = ?");
            $stmt->bind_param("s", $id_deskripsi);
            if($stmt->execute())
                return true; 
            return false; 
        }
	
}