<?php

class KategoriOperation{
	
	private $con; 

        function __construct(){
            require_once dirname(__FILE__) . '/DbConnect.php';
            $db = new DbConnect; 
            $this->con = $db->connect(); 
        }
		
		public function createKategori($nama_kat_produk){
           if(!$this->jikaKategoriAda($nama_kat_produk)){
                $stmt = $this->con->prepare("INSERT INTO tb_kat_produk (nama_kat_produk) VALUES (?)");
                $stmt->bind_param("s", $nama_kat_produk);
                if($stmt->execute()){
                    return KATEGORI_BERHASIL_DIBUAT; 
                }else{
                    return KATEGORI_GAGAL_DIBUAT;
                }
           }
           return KATEGORI_SUDAH_ADA; 
        }
		
		private function jikaKategoriAda($nama_kat_produk){
            $stmt = $this->con->prepare("SELECT id_kat_produk FROM tb_kat_produk WHERE nama_kat_produk = ?");
            $stmt->bind_param("s", $nama_kat_produk);
            $stmt->execute(); 
            $stmt->store_result(); 
            return $stmt->num_rows > 0;  
        }
		
		public function ListKategori(){
            $stmt = $this->con->prepare("SELECT id_kat_produk,nama_kat_produk FROM tb_kat_produk;");
            $stmt->execute();
            $stmt->bind_result($id_kat_produk, $nama_kat_produk);
            $ArrayKategori = array(); 
            while($stmt->fetch()){ 
                $kategori = array(); 
                $kategori['id_kat_produk']=$id_kat_produk; 
                $kategori['nama_kat_produk']=$nama_kat_produk; 
              
                array_push($ArrayKategori, $kategori);
            }             
            return $ArrayKategori; 
        }
		
		public function updateKategori($id_kat_produk, $nama_kat_produk){
            $stmt = $this->con->prepare("UPDATE tb_kat_produk SET nama_kat_produk = ? WHERE id_kat_produk = ?");
            $stmt->bind_param("ss",$nama_kat_produk, $id_kat_produk);
            if($stmt->execute())
                return true; 
            return false; 
        }
		
		public function getKategoriByID($id_kat_produk){
            $stmt = $this->con->prepare("SELECT nama_kat_produk FROM tb_kat_produk WHERE id_kat_produk = ?");
            $stmt->bind_param("s", $id_kat_produk);
            $stmt->execute(); 
            $stmt->bind_result($nama_kat_produk);
            $stmt->fetch(); 
            $kategori = array(); 
            $kategori['id_kat_produk'] = $id_kat_produk; 
            $kategori['nama_kat_produk']=$nama_kat_produk; 
           
            return $kategori; 
        }
		private function getNamaKategoriById($id_kat_produk){
            $stmt = $this->con->prepare("SELECT nama_kat_produk FROM tb_kat_produk WHERE id_kat_produk = ?");
            $stmt->bind_param("i", $id_kat_produk);
            $stmt->execute(); 
            $stmt->bind_result($nama_kat_produk);
            $stmt->fetch(); 
            return $nama_kat_produk; 
        }
		
		public function hapusKategori($nama_kat_produk){
            $stmt = $this->con->prepare("DELETE FROM tb_kat_produk WHERE nama_kat_produk = ?");
            $stmt->bind_param("s", $nama_kat_produk);
            if($stmt->execute())
                return true; 
            return false; 
        }
		/*public function createTableKategori($namaTabelKategori){
			$sql = "CREATE TABLE {$namaTabelKategori} (
					id INT(6) UNSIGNED AUTO_INCREMENT PRIMARY KEY,
					nama_produk VARCHAR(30) NOT NULL,
					harga_produk INT(30) NOT NULL,
					jumlah_produk INT(50),
					kategori_produk VARCHAR(30),
					gambar_produk LONGTEXT
					)";
			$stmt = $this->con->prepare($sql);  
			//$stmt->execute();			
            if($stmt->execute()){
                    return TABEL_BERHASIL_DIBUAT;
                }else{
                    return TABEL_GAGAL_DIBUAT;
                }
		
		}
		
		public function updateNamaTabelKategori($id,$nama_kategori){
			$nama_kategori_lama = $this->getNamaKategoriById($id); 
            $stmt = $this->con->prepare("RENAME TABLE {$nama_kategori_lama} TO {$nama_kategori}");
            if($stmt->execute()){
                return true; 
			}else{
            return false; 
			}
        }*/		
		
		
		/*public function hapusTabelKategori($nama_kategori){
			$stmt = $this->con->prepare("DROP TABLE {$nama_kategori}");
            if($stmt->execute())
                return true; 
            return false; 
		}*/
}