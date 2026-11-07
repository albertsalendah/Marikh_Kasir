<?php

class DetailPenjualanOperation{
	
	private $con; 

        function __construct(){
            require_once dirname(__FILE__) . '/DbConnect.php';
            $db = new DbConnect; 
            $this->con = $db->connect(); 
        }
		
		public function InsertDetailPenjualan($id_trx_detpenjualan,$id_trx_penjualan,$qty_terjual,$harga_terjual,$diskon_terjual,$status_pesanan,$id_produk,$jenis_transaksi,$deskripsi,$catatan_tambahan){
			if(!$this->jikaIDSudahAda($id_trx_detpenjualan)){
                $stmt = $this->con->prepare("INSERT INTO tb_trx_detpenjualan (id_trx_detpenjualan,id_trx_penjualan,qty_terjual,harga_terjual,diskon_terjual,status_pesanan,id_produk,jenis_transaksi,deskripsi,catatan_tambahan) VALUES (?,?,?,?,?,?,?,?,?,?)");
                $stmt->bind_param("ssssssssss",$id_trx_detpenjualan,$id_trx_penjualan,$qty_terjual,$harga_terjual,$diskon_terjual,$status_pesanan,$id_produk,$jenis_transaksi,$deskripsi,$catatan_tambahan);
                if($stmt->execute()){
                    return DETAIL_TRANSAKSI_BERHASIL_DITAMBAH; 
                }else{
                    return DETAIL_TRANSAKSI_GAGAL_DITAMBAH;
                }
			}
			return DETAIL_TRANSAKSI_SUDAH_ADA;
        }
		
		private function jikaIDSudahAda($id_trx_detpenjualan){
            $stmt = $this->con->prepare("SELECT id_trx_detpenjualan FROM tb_trx_detpenjualan WHERE id_trx_detpenjualan = ?");
            $stmt->bind_param("s", $id_trx_detpenjualan);
            $stmt->execute(); 
            $stmt->store_result(); 
            return $stmt->num_rows > 0;  
        }
		
		public function getDetailTrxPenjualanByID($id_trx_penjualan){
            $stmt = $this->con->prepare("SELECT id_trx_detpenjualan,id_trx_penjualan,qty_terjual,harga_terjual,diskon_terjual,status_pesanan,id_produk,jenis_transaksi,deskripsi,catatan_tambahan FROM tb_trx_detpenjualan WHERE id_trx_penjualan = ?");
            $stmt->bind_param("s", $id_trx_penjualan);
            $stmt->execute(); 
            $stmt->bind_result($id_trx_detpenjualan,$id_trx_penjualan,$qty_terjual,$harga_terjual,$diskon_terjual,$status_pesanan,$id_produk,$jenis_transaksi,$deskripsi,$catatan_tambahan);       
            $ArrayDetaiPenjualan = array(); 
			while($stmt->fetch()){ 
                $DetailTrxPenjualan = array(); 
				$DetailTrxPenjualan['id_trx_detpenjualan'] = $id_trx_detpenjualan; 
                $DetailTrxPenjualan['id_trx_penjualan'] = $id_trx_penjualan; 
                $DetailTrxPenjualan['qty_terjual'] = $qty_terjual; 
                $DetailTrxPenjualan['harga_terjual'] = $harga_terjual; 
				$DetailTrxPenjualan['diskon_terjual'] = $diskon_terjual;
				$DetailTrxPenjualan['status_pesanan'] = $status_pesanan;
				$DetailTrxPenjualan['id_produk'] = $id_produk;
				$DetailTrxPenjualan['jenis_transaksi'] = $jenis_transaksi;
				$DetailTrxPenjualan['deskripsi'] = $deskripsi;
				$DetailTrxPenjualan['catatan_tambahan'] = $catatan_tambahan;
				array_push($ArrayDetaiPenjualan, $DetailTrxPenjualan);
			}           
            return $ArrayDetaiPenjualan; 
        }
		
		public function ListDetailTrxPenjualan(){
            $stmt = $this->con->prepare("SELECT id_trx_detpenjualan,id_trx_penjualan,qty_terjual,harga_terjual,diskon_terjual,status_pesanan,id_produk,jenis_transaksi,deskripsi,catatan_tambahan FROM tb_trx_detpenjualan;");
            $stmt->execute(); 
            $stmt->bind_result($id_trx_detpenjualan,$id_trx_penjualan,$qty_terjual,$harga_terjual,$diskon_terjual,$status_pesanan,$id_produk,$jenis_transaksi,$deskripsi,$catatan_tambahan);       
            $ArrayDetaiPenjualan = array(); 
			while($stmt->fetch()){ 
                $DetailTrxPenjualan = array(); 
				$DetailTrxPenjualan['id_trx_detpenjualan'] = $id_trx_detpenjualan; 
                $DetailTrxPenjualan['id_trx_penjualan'] = $id_trx_penjualan; 
                $DetailTrxPenjualan['qty_terjual'] = $qty_terjual; 
                $DetailTrxPenjualan['harga_terjual'] = $harga_terjual; 
				$DetailTrxPenjualan['diskon_terjual'] = $diskon_terjual;
				$DetailTrxPenjualan['status_pesanan'] = $status_pesanan;
				$DetailTrxPenjualan['id_produk'] = $id_produk;
				$DetailTrxPenjualan['jenis_transaksi'] = $jenis_transaksi;
				$DetailTrxPenjualan['deskripsi'] = $deskripsi;
				$DetailTrxPenjualan['catatan_tambahan'] = $catatan_tambahan;
				array_push($ArrayDetaiPenjualan, $DetailTrxPenjualan);
			}           
            return $ArrayDetaiPenjualan; 
        }
		
}