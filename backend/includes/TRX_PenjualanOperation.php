<?php

class TRX_PenjualanOperation{
	
	private $con; 

        function __construct(){
            require_once dirname(__FILE__) . '/DbConnect.php';
            $db = new DbConnect; 
            $this->con = $db->connect(); 
        }
		
		public function InsertTransaksi($id_trx_penjualan,$tgl_trx_input,$tgl_trx_bayar,$tagihan_akhir,$tagihan_awal,$total_diskon,$tax,$terbayar,$komisi,$id_pelanggan,$nama,$no_meja,$id_method_bayar,$jenis_tagihan,$id_user){
			if(!$this->jikaIDSudahAda($id_trx_penjualan)){            
				$stmt = $this->con->prepare("INSERT INTO tb_trx_penjualan (id_trx_penjualan,tgl_trx_input,tgl_trx_bayar,tagihan_akhir,tagihan_awal,total_diskon,tax,terbayar,komisi,id_pelanggan,nama,no_meja,id_method_bayar,jenis_tagihan,id_user) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");			
                $stmt->bind_param("sssssssssssssss", $id_trx_penjualan,$tgl_trx_input,$tgl_trx_bayar,$tagihan_akhir,$tagihan_awal,$total_diskon,$tax,$terbayar,$komisi,$id_pelanggan,$nama,$no_meja,$id_method_bayar,$jenis_tagihan,$id_user);
                if($stmt->execute()){
                    return TRANSAKSI_BERHASI_DITAMBAH; 
                }else{
                    return TRANSAKSI_GAGAL_DITAMBAH;
                }
			}
			return TRANSAKSI_SUDAH_ADA;
        }
		
		private function jikaIDSudahAda($id_trx_penjualan){
            $stmt = $this->con->prepare("SELECT id_trx_penjualan FROM tb_trx_penjualan WHERE id_trx_penjualan = ?");
            $stmt->bind_param("s", $id_trx_penjualan);
            $stmt->execute(); 
            $stmt->store_result(); 
            return $stmt->num_rows > 0;  
        }
		
		public function ListTrxPenjualan(){
            $stmt = $this->con->prepare("SELECT id_trx_penjualan,tgl_trx_input,tgl_trx_bayar,tagihan_akhir,tagihan_awal,total_diskon,tax,terbayar,komisi,id_pelanggan,nama,no_meja,id_method_bayar,jenis_tagihan,id_user FROM tb_trx_penjualan;");
            $stmt->execute();
            $stmt->bind_result($id_trx_penjualan,$tgl_trx_input,$tgl_trx_bayar,$tagihan_akhir,$tagihan_awal,$total_diskon,$tax,$terbayar,$komisi,$id_pelanggan,$nama,$no_meja,$id_method_bayar,$jenis_tagihan,$id_user);
            $ArrayTrxPenjualan = array(); 
            while($stmt->fetch()){ 
                $TrxPenjualan = array(); 
				$TrxPenjualan['id_trx_penjualan'] = $id_trx_penjualan; 
                $TrxPenjualan['tgl_trx_input'] = $tgl_trx_input; 
                $TrxPenjualan['tgl_trx_bayar'] = $tgl_trx_bayar; 
                $TrxPenjualan['tagihan_akhir'] = $tagihan_akhir; 
				$TrxPenjualan['tagihan_awal'] = $tagihan_awal;
				$TrxPenjualan['total_diskon'] = $total_diskon;
				$TrxPenjualan['tax'] = $tax;
				$TrxPenjualan['terbayar'] = $terbayar;
				$TrxPenjualan['komisi'] = $komisi;
				$TrxPenjualan['id_pelanggan'] = $id_pelanggan;
				$TrxPenjualan['nama'] = $nama;
				$TrxPenjualan['no_meja'] = $no_meja;
				$TrxPenjualan['id_method_bayar'] = $id_method_bayar;
				$TrxPenjualan['jenis_tagihan'] = $jenis_tagihan;
				$TrxPenjualan['id_user'] = $id_user;
                array_push($ArrayTrxPenjualan, $TrxPenjualan);
            }             
            return $ArrayTrxPenjualan; 
        }
		
		public function getTrxPenjualanByJenisTagihan($jenis_tagihan){
            $stmt = $this->con->prepare("SELECT id_trx_penjualan,tgl_trx_input,tgl_trx_bayar,tagihan_akhir,tagihan_awal,total_diskon,tax,terbayar,komisi,id_pelanggan,nama,no_meja,id_method_bayar,jenis_tagihan,id_user FROM tb_trx_penjualan WHERE jenis_tagihan = ?");
            $stmt->bind_param("s", $jenis_tagihan);
            $stmt->execute(); 
            $stmt->bind_result($id_trx_penjualan,$tgl_trx_input,$tgl_trx_bayar,$tagihan_akhir,$tagihan_awal,$total_diskon,$tax,$terbayar,$komisi,$id_pelanggan,$nama,$no_meja,$id_method_bayar,$jenis_tagihan,$id_user);
            $ArrayTrxPenjualan = array(); 
            while($stmt->fetch()){ 
                $TrxPenjualan = array(); 
				$TrxPenjualan['id_trx_penjualan'] = $id_trx_penjualan; 
                $TrxPenjualan['tgl_trx_input'] = $tgl_trx_input; 
                $TrxPenjualan['tgl_trx_bayar'] = $tgl_trx_bayar; 
                $TrxPenjualan['tagihan_akhir'] = $tagihan_akhir; 
				$TrxPenjualan['tagihan_awal'] = $tagihan_awal;
				$TrxPenjualan['total_diskon'] = $total_diskon;
				$TrxPenjualan['tax'] = $tax;
				$TrxPenjualan['terbayar'] = $terbayar;
				$TrxPenjualan['komisi'] = $komisi;
				$TrxPenjualan['id_pelanggan'] = $id_pelanggan;
				$TrxPenjualan['nama'] = $nama;
				$TrxPenjualan['no_meja'] = $no_meja;
				$TrxPenjualan['id_method_bayar'] = $id_method_bayar;
				$TrxPenjualan['jenis_tagihan'] = $jenis_tagihan;
				$TrxPenjualan['id_user'] = $id_user;
                array_push($ArrayTrxPenjualan, $TrxPenjualan);
            }             
            return $ArrayTrxPenjualan; 
        }
		
		public function getTrxPenjualanByNamaPelanggan($nama){
            $stmt = $this->con->prepare("SELECT id_trx_penjualan,tgl_trx_input,tgl_trx_bayar,tagihan_akhir,tagihan_awal,total_diskon,tax,terbayar,komisi,id_pelanggan,nama,no_meja,id_method_bayar,jenis_tagihan,id_user FROM tb_trx_penjualan WHERE nama = ?");
            $stmt->bind_param("s", $nama);
            $stmt->execute(); 
            $stmt->bind_result($id_trx_penjualan,$tgl_trx_input,$tgl_trx_bayar,$tagihan_akhir,$tagihan_awal,$total_diskon,$tax,$terbayar,$komisi,$id_pelanggan,$nama,$no_meja,$id_method_bayar,$jenis_tagihan,$id_user);
            $ArrayTrxPenjualan = array(); 
            while($stmt->fetch()){ 
                $TrxPenjualan = array(); 
				$TrxPenjualan['id_trx_penjualan'] = $id_trx_penjualan; 
                $TrxPenjualan['tgl_trx_input'] = $tgl_trx_input; 
                $TrxPenjualan['tgl_trx_bayar'] = $tgl_trx_bayar; 
                $TrxPenjualan['tagihan_akhir'] = $tagihan_akhir; 
				$TrxPenjualan['tagihan_awal'] = $tagihan_awal;
				$TrxPenjualan['total_diskon'] = $total_diskon;
				$TrxPenjualan['tax'] = $tax;
				$TrxPenjualan['terbayar'] = $terbayar;
				$TrxPenjualan['komisi'] = $komisi;
				$TrxPenjualan['id_pelanggan'] = $id_pelanggan;
				$TrxPenjualan['nama'] = $nama;
				$TrxPenjualan['no_meja'] = $no_meja;
				$TrxPenjualan['id_method_bayar'] = $id_method_bayar;
				$TrxPenjualan['jenis_tagihan'] = $jenis_tagihan;
				$TrxPenjualan['id_user'] = $id_user;
                array_push($ArrayTrxPenjualan, $TrxPenjualan);
            }             
            return $ArrayTrxPenjualan; 
        }
		
		public function getTrxPenjualanByID($id_trx_penjualan){
            $stmt = $this->con->prepare("SELECT id_trx_penjualan,tgl_trx_input,tgl_trx_bayar,tagihan_akhir,tagihan_awal,total_diskon,tax,terbayar,komisi,id_pelanggan,nama,no_meja,id_method_bayar,jenis_tagihan,id_user FROM tb_trx_penjualan WHERE id_trx_penjualan = ?");
            $stmt->bind_param("s", $id_trx_penjualan);
            $stmt->execute(); 
            $stmt->bind_result($id_trx_penjualan,$tgl_trx_input,$tgl_trx_bayar,$tagihan_akhir,$tagihan_awal,$total_diskon,$tax,$terbayar,$komisi,$id_pelanggan,$nama,$no_meja,$id_method_bayar,$jenis_tagihan,$id_user);       
            $stmt->fetch();
                $TrxPenjualan = array(); 
				$TrxPenjualan['id_trx_penjualan'] = $id_trx_penjualan; 
                $TrxPenjualan['tgl_trx_input'] = $tgl_trx_input; 
                $TrxPenjualan['tgl_trx_bayar'] = $tgl_trx_bayar; 
                $TrxPenjualan['tagihan_akhir'] = $tagihan_akhir; 
				$TrxPenjualan['tagihan_awal'] = $tagihan_awal;
				$TrxPenjualan['total_diskon'] = $total_diskon;
				$TrxPenjualan['tax'] = $tax;
				$TrxPenjualan['terbayar'] = $terbayar;
				$TrxPenjualan['komisi'] = $komisi;
				$TrxPenjualan['id_pelanggan'] = $id_pelanggan;
				$TrxPenjualan['nama'] = $nama;
				$TrxPenjualan['no_meja'] = $no_meja;
				$TrxPenjualan['id_method_bayar'] = $id_method_bayar;
				$TrxPenjualan['jenis_tagihan'] = $jenis_tagihan;
				$TrxPenjualan['id_user'] = $id_user;                    
            return $TrxPenjualan; 
        }
		
		public function updateTrxPenjualan($id_trx_penjualan,$tgl_trx_bayar,$tax,$terbayar,$id_method_bayar,$jenis_tagihan){
            $stmt = $this->con->prepare("UPDATE tb_trx_penjualan SET tgl_trx_bayar = ?,tax = ?,terbayar = ?,id_method_bayar = ?,jenis_tagihan = ? WHERE id_trx_penjualan = ?");
            $stmt->bind_param("ssssss",$tgl_trx_bayar,$tax,$terbayar,$id_method_bayar,$jenis_tagihan,$id_trx_penjualan);
            if($stmt->execute())
                return true; 
            return false; 
        }
		
		public function hapusTrxPenjualan($id_trx_penjualan){
            $stmt = $this->con->prepare("DELETE FROM tb_trx_penjualan WHERE id_trx_penjualan = ?");
            $stmt->bind_param("s", $id_trx_penjualan);
            if($stmt->execute())
                return true; 
            return false; 
        }
		
		public function TambahPesanan($id_trx_penjualan,$tgl_trx_input,$tagihan_akhir,$tagihan_awal,$total_diskon,$tax){
            $stmt = $this->con->prepare("UPDATE tb_trx_penjualan SET tgl_trx_input = ?,tagihan_akhir = ?,tagihan_awal = ?,total_diskon = ?,tax = ? WHERE id_trx_penjualan = ?");
            $stmt->bind_param("ssssss",$tgl_trx_input,$tagihan_akhir,$tagihan_awal,$total_diskon,$tax,$id_trx_penjualan);
            if($stmt->execute())
                return true; 
            return false; 
        }
		
}