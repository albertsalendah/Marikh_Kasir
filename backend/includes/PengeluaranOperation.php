<?php

	class PengeluaranOperation{
		function __construct(){
            require_once dirname(__FILE__) . '/DbConnect.php';
            $db = new DbConnect; 
            $this->con = $db->connect(); 
        }
		
		public function tambahPengeluaran($id_kat_pengeluaran,$nama_pengeluaran,$tgl_pengeluaran,$nominal_pengeluaran,$qty_pengeluaran,$total_pengeluaran,$jenis_post_pengeluaran){
				
			$stmt = $this->con->prepare("INSERT INTO tb_trx_pengeluaran (id_kat_pengeluaran,nama_pengeluaran,tgl_pengeluaran,nominal_pengeluaran,qty_pengeluaran,total_pengeluaran,jenis_post_pengeluaran) VALUES (?,?,?,?,?,?,?)");
			$stmt->bind_param("sssssss",$id_kat_pengeluaran,$nama_pengeluaran,$tgl_pengeluaran,$nominal_pengeluaran,$qty_pengeluaran,$total_pengeluaran,$jenis_post_pengeluaran);
			if($stmt->execute()){
				return true; 
			}else{
				return false;
			}
		}
		
		public function updatePengeluaran($id_trx_pengeluaran,$id_kat_pengeluaran,$nama_pengeluaran,$tgl_pengeluaran,$nominal_pengeluaran,$qty_pengeluaran,$total_pengeluaran,$jenis_post_pengeluaran){
				$stmt = $this->con->prepare("UPDATE tb_trx_pengeluaran SET id_kat_pengeluaran = ?,nama_pengeluaran = ?,tgl_pengeluaran = ?,nominal_pengeluaran = ?,qty_pengeluaran = ?,total_pengeluaran = ?,jenis_post_pengeluaran = ? WHERE id_trx_pengeluaran = ?");
				$stmt->bind_param("ssssssss",$id_kat_pengeluaran,$nama_pengeluaran,$tgl_pengeluaran,$nominal_pengeluaran,$qty_pengeluaran,$total_pengeluaran,$jenis_post_pengeluaran,$id_trx_pengeluaran);
				if($stmt->execute())
					return true; 
				return false;			
        }
		
		public function getPengeluaranByID($id_trx_pengeluaran){
            $stmt = $this->con->prepare("SELECT id_trx_pengeluaran,id_kat_pengeluaran,nama_pengeluaran,tgl_pengeluaran,nominal_pengeluaran,qty_pengeluaran,total_pengeluaran,jenis_post_pengeluaran FROM tb_trx_pengeluaran WHERE id_trx_pengeluaran = ?");
            $stmt->bind_param("s", $id_trx_pengeluaran);
            $stmt->execute(); 
            $stmt->bind_result($id_trx_pengeluaran,$id_kat_pengeluaran,$nama_pengeluaran,$tgl_pengeluaran,$nominal_pengeluaran,$qty_pengeluaran,$total_pengeluaran,$jenis_post_pengeluaran);
            $stmt->fetch(); 
            $Pengeluaran = array(); 
                $Pengeluaran['id_trx_pengeluaran']=$id_trx_pengeluaran; 
				$Pengeluaran['id_kat_pengeluaran']=$id_kat_pengeluaran; 
				$Pengeluaran['nama_pengeluaran']=$nama_pengeluaran; 
				$Pengeluaran['tgl_pengeluaran']=$tgl_pengeluaran; 
				$Pengeluaran['nominal_pengeluaran']=$nominal_pengeluaran; 
                $Pengeluaran['qty_pengeluaran']=$qty_pengeluaran; 
				$Pengeluaran['total_pengeluaran']=$total_pengeluaran;
				$Pengeluaran['jenis_post_pengeluaran']=$jenis_post_pengeluaran;
				
            return $Pengeluaran; 
        }
		
		public function allPengeluaran(){
            $stmt = $this->con->prepare("SELECT id_trx_pengeluaran,id_kat_pengeluaran,nama_pengeluaran,tgl_pengeluaran,nominal_pengeluaran,qty_pengeluaran,total_pengeluaran,jenis_post_pengeluaran FROM tb_trx_pengeluaran");
			$stmt->execute(); 
            $stmt->bind_result($id_trx_pengeluaran,$id_kat_pengeluaran,$nama_pengeluaran,$tgl_pengeluaran,$nominal_pengeluaran,$qty_pengeluaran,$total_pengeluaran,$jenis_post_pengeluaran);
            $ArrayPengeluaran = array(); 
            while($stmt->fetch()){ 
                $Pengeluaran = array(); 
                $Pengeluaran['id_trx_pengeluaran']=$id_trx_pengeluaran; 
				$Pengeluaran['id_kat_pengeluaran']=$id_kat_pengeluaran; 
				$Pengeluaran['nama_pengeluaran']=$nama_pengeluaran; 
				$Pengeluaran['tgl_pengeluaran']=$tgl_pengeluaran; 
				$Pengeluaran['nominal_pengeluaran']=$nominal_pengeluaran; 
                $Pengeluaran['qty_pengeluaran']=$qty_pengeluaran; 
				$Pengeluaran['total_pengeluaran']=$total_pengeluaran;
				$Pengeluaran['jenis_post_pengeluaran']=$jenis_post_pengeluaran;
              
                array_push($ArrayPengeluaran, $Pengeluaran);
            }             
            return $ArrayPengeluaran; 
        }
		
		
		public function tambahKategoriPengeluaran($nama_kat_pengeluaran){
				
			$stmt = $this->con->prepare("INSERT INTO tb_kat_pengeluaran (nama_kat_pengeluaran) VALUES (?)");
			$stmt->bind_param("s",$nama_kat_pengeluaran);
			if($stmt->execute()){
				return true; 
			}else{
				return false;
			}
		}
		
		public function updateKategoriPengeluaran($id_kat_pengeluaran,$nama_kat_pengeluaran){
				$stmt = $this->con->prepare("UPDATE tb_kat_pengeluaran SET nama_kat_pengeluaran = ? WHERE id_kat_pengeluaran = ?");
				$stmt->bind_param("ss",$nama_kat_pengeluaran,$id_kat_pengeluaran);
				if($stmt->execute())
					return true; 
				return false;			
        }
		
		public function getKategoriPengeluaranByID($id_kat_pengeluaran){
            $stmt = $this->con->prepare("SELECT id_kat_pengeluaran,nama_kat_pengeluaran FROM tb_kat_pengeluaran WHERE id_kat_pengeluaran = ?");
            $stmt->bind_param("s", $id_kat_pengeluaran);
            $stmt->execute(); 
            $stmt->bind_result($id_kat_pengeluaran,$nama_kat_pengeluaran);
            $stmt->fetch(); 
            $KategoriPengeluaran = array(); 
				$KategoriPengeluaran['id_kat_pengeluaran']=$id_kat_pengeluaran; 
				$KategoriPengeluaran['nama_kat_pengeluaran']=$nama_kat_pengeluaran; 
				
            return $KategoriPengeluaran; 
        }
		
		public function allKategoriPengeluaran(){
            $stmt = $this->con->prepare("SELECT id_kat_pengeluaran,nama_kat_pengeluaran FROM tb_kat_pengeluaran");
			$stmt->execute(); 
            $stmt->bind_result($id_kat_pengeluaran,$nama_kat_pengeluaran);
            $ArrayKategoriPengeluaran = array(); 
            while($stmt->fetch()){ 
                $KategoriPengeluaran = array(); 
                $KategoriPengeluaran['id_kat_pengeluaran']=$id_kat_pengeluaran; 
				$KategoriPengeluaran['nama_kat_pengeluaran']=$nama_kat_pengeluaran; 
              
                array_push($ArrayKategoriPengeluaran, $KategoriPengeluaran);
            }             
            return $ArrayKategoriPengeluaran; 
        }
		
	}