<?php

	class ProdukOperation{
		function __construct(){
            require_once dirname(__FILE__) . '/DbConnect.php';
            $db = new DbConnect; 
            $this->con = $db->connect(); 
        }
		
		public function tambahproduct($nama_produk,$harga_produk,$qty_produk,$diskon_produk,$id_kat_produk,$tgl_awal_diskon,$tgl_akhir_diskon,$diskon_olfood,$tgl_awal_diskon_olfood,$tgl_akhir_diskon_olfood,$gbr_produk){
				if(!$this->ceknamaproduk($nama_produk)){
					$stmt = $this->con->prepare("INSERT INTO tb_produk (nama_produk,harga_produk,qty_produk,diskon_produk,id_kat_produk,tgl_awal_diskon,tgl_akhir_diskon,diskon_olfood,tgl_awal_diskon_olfood,tgl_akhir_diskon_olfood,gbr_produk) VALUES (?,?,?,?,?,?,?,?,?,?,?)");
					$stmt->bind_param("sssssssssss",$nama_produk,$harga_produk,$qty_produk,$diskon_produk,$id_kat_produk,$tgl_awal_diskon,$tgl_akhir_diskon,$diskon_olfood,$tgl_awal_diskon_olfood,$tgl_akhir_diskon_olfood,$gbr_produk);
					if($stmt->execute()){
						return PRODUK_BERHASIL_DITAMBAH; 
						}else{
						return PRODUK_GAGAL_DITAMBAH;
					}
				}else{
					return PRODUK_SUDAH_ADA;
				}
		}
		private function ceknamaproduk($nama_produk){
            $stmt = $this->con->prepare("SELECT nama_produk FROM tb_produk WHERE nama_produk = ?");
            $stmt->bind_param("s", $nama_produk);
            $stmt->execute(); 
            $stmt->store_result(); 
            return $stmt->num_rows > 0;  
        }
		public function daftarproduk($id_kat_produk){
            $stmt = $this->con->prepare("SELECT id_produk,nama_produk,harga_produk,qty_produk,diskon_produk,id_kat_produk,tgl_awal_diskon,tgl_akhir_diskon,diskon_olfood,tgl_awal_diskon_olfood,tgl_akhir_diskon_olfood,gbr_produk FROM tb_produk WHERE id_kat_produk = ?");
            $stmt->bind_param("s", $id_kat_produk);
			$stmt->execute(); 
            $stmt->bind_result($id_produk,$nama_produk,$harga_produk,$qty_produk,$diskon_produk,$id_kat_produk,$tgl_awal_diskon,$tgl_akhir_diskon,$diskon_olfood,$tgl_awal_diskon_olfood,$tgl_akhir_diskon_olfood,$gbr_produk);
            $ArrayProduk = array(); 
            while($stmt->fetch()){ 
                $Produk = array(); 
                $Produk['id_produk']=$id_produk; 
				$Produk['nama_produk']=$nama_produk; 
				$Produk['harga_produk']=$harga_produk; 
				$Produk['qty_produk']=$qty_produk; 
				$Produk['diskon_produk']=$diskon_produk; 
                $Produk['id_kat_produk']=$id_kat_produk; 
				$Produk['tgl_awal_diskon']=$tgl_awal_diskon;
				$Produk['tgl_akhir_diskon']=$tgl_akhir_diskon;
				$Produk['diskon_olfood']=$diskon_olfood; 
				$Produk['tgl_awal_diskon_olfood']=$tgl_awal_diskon_olfood;
				$Produk['tgl_akhir_diskon_olfood']=$tgl_akhir_diskon_olfood;
				$Produk['gbr_produk']=$gbr_produk;
              
                array_push($ArrayProduk, $Produk);
            }             
            return $ArrayProduk; 
        }
		
		public function allproduk(){
            $stmt = $this->con->prepare("SELECT id_produk,nama_produk,harga_produk,qty_produk,diskon_produk,id_kat_produk,tgl_awal_diskon,tgl_akhir_diskon,diskon_olfood,tgl_awal_diskon_olfood,tgl_akhir_diskon_olfood,gbr_produk FROM tb_produk");
			$stmt->execute(); 
            $stmt->bind_result($id_produk,$nama_produk,$harga_produk,$qty_produk,$diskon_produk,$id_kat_produk,$tgl_awal_diskon,$tgl_akhir_diskon,$diskon_olfood,$tgl_awal_diskon_olfood,$tgl_akhir_diskon_olfood,$gbr_produk);
            $ArrayProduk = array(); 
            while($stmt->fetch()){ 
                $Produk = array(); 
                $Produk['id_produk']=$id_produk; 
				$Produk['nama_produk']=$nama_produk; 
				$Produk['harga_produk']=$harga_produk; 
				$Produk['qty_produk']=$qty_produk; 
				$Produk['diskon_produk']=$diskon_produk; 
                $Produk['id_kat_produk']=$id_kat_produk; 
				$Produk['tgl_awal_diskon']=$tgl_awal_diskon;
				$Produk['tgl_akhir_diskon']=$tgl_akhir_diskon;
				$Produk['diskon_olfood']=$diskon_olfood; 
				$Produk['tgl_awal_diskon_olfood']=$tgl_awal_diskon_olfood;
				$Produk['tgl_akhir_diskon_olfood']=$tgl_akhir_diskon_olfood;
				$Produk['gbr_produk']=$gbr_produk;
              
                array_push($ArrayProduk, $Produk);
            }             
            return $ArrayProduk; 
        }
		
		public function getProdukByID($id_produk){
            $stmt = $this->con->prepare("SELECT id_produk,nama_produk,harga_produk,qty_produk,diskon_produk,id_kat_produk,tgl_awal_diskon,tgl_akhir_diskon,diskon_olfood,tgl_awal_diskon_olfood,tgl_akhir_diskon_olfood,gbr_produk FROM tb_produk WHERE id_produk = ?");
            $stmt->bind_param("s", $id_produk);
            $stmt->execute(); 
            $stmt->bind_result($id_produk,$nama_produk,$harga_produk,$qty_produk,$diskon_produk,$id_kat_produk,$tgl_awal_diskon,$tgl_akhir_diskon,$diskon_olfood,$tgl_awal_diskon_olfood,$tgl_akhir_diskon_olfood,$gbr_produk);
            $stmt->fetch(); 
            $Produk = array(); 
				$Produk['id_produk']=$id_produk; 
				$Produk['nama_produk']=$nama_produk; 
				$Produk['harga_produk']=$harga_produk; 
				$Produk['qty_produk']=$qty_produk; 
				$Produk['diskon_produk']=$diskon_produk; 
                $Produk['id_kat_produk']=$id_kat_produk; 
				$Produk['tgl_awal_diskon']=$tgl_awal_diskon;
				$Produk['tgl_akhir_diskon']=$tgl_akhir_diskon;
				$Produk['diskon_olfood']=$diskon_olfood; 
				$Produk['tgl_awal_diskon_olfood']=$tgl_awal_diskon_olfood;
				$Produk['tgl_akhir_diskon_olfood']=$tgl_akhir_diskon_olfood;
				$Produk['gbr_produk']=$gbr_produk;
				
            return $Produk; 
        }
		
		public function updateProduk($id_produk,$nama_produk,$harga_produk,$qty_produk,$diskon_produk,$id_kat_produk,$tgl_awal_diskon,$tgl_akhir_diskon,$diskon_olfood,$tgl_awal_diskon_olfood,$tgl_akhir_diskon_olfood,$gbr_produk){
				$stmt = $this->con->prepare("UPDATE tb_produk SET nama_produk = ?,harga_produk = ?,qty_produk = ?,diskon_produk = ?,id_kat_produk = ?,tgl_awal_diskon = ?,tgl_akhir_diskon = ?,diskon_olfood = ?,tgl_awal_diskon_olfood = ?,tgl_akhir_diskon_olfood = ?,gbr_produk = ? WHERE id_produk = ?");
				$stmt->bind_param("ssssssssssss",$nama_produk,$harga_produk,$qty_produk,$diskon_produk,$id_kat_produk,$tgl_awal_diskon,$tgl_akhir_diskon,$diskon_olfood,$tgl_awal_diskon_olfood,$tgl_akhir_diskon_olfood,$gbr_produk,$id_produk);
				if($stmt->execute())
					return true; 
				return false;			
        }
		
		public function hapusproduk($id_produk){
            $stmt = $this->con->prepare("DELETE FROM tb_produk WHERE id_produk = ?");
            $stmt->bind_param("s", $id_produk);
            if($stmt->execute())
                return true; 
            return false; 
        }
	}