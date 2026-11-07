<?php


use \Psr\Http\Message\ServerRequestInterface as Request;
use \Psr\Http\Message\ResponseInterface as Response;

require '../vendor/autoload.php';

require '../includes/TRX_PenjualanOperation.php';


$app = new \Slim\App([
    'settings'=>[
        'displayErrorDetails'=>true
    ]
]);

$app->get('/daftartrxpenjualan', function(Request $request, Response $response){
	
	$db = new TRX_PenjualanOperation; 

    $TrxPenjualan = $db->ListTrxPenjualan();

    $response->write(json_encode($TrxPenjualan));

    return $response
    ->withHeader('Content-type', 'application/json')
    ->withStatus(200);  
	    
});

$app->post('/getjenistagihan', function(Request $request, Response $response){
	
	$db = new TRX_PenjualanOperation; 
	
	$request_data = $request->getParsedBody(); 
		
	$jenis_tagihan = $request_data['jenis_tagihan'];

    $TrxPenjualan = $db->getTrxPenjualanByJenisTagihan($jenis_tagihan);

    $response->write(json_encode($TrxPenjualan));

    return $response
    ->withHeader('Content-type', 'application/json')
    ->withStatus(200);  
	    
});

$app->post('/gettagihanbynama', function(Request $request, Response $response){
	
	$db = new TRX_PenjualanOperation; 
	
	$request_data = $request->getParsedBody(); 
		
	$nama = $request_data['nama'];

    $TrxPenjualan = $db->getTrxPenjualanByNamaPelanggan($nama);

    $response->write(json_encode($TrxPenjualan));

    return $response
    ->withHeader('Content-type', 'application/json')
    ->withStatus(200);  
	    
});

$app->post('/getidtransaksi', function(Request $request, Response $response){
	
	$db = new TRX_PenjualanOperation; 
	
	$request_data = $request->getParsedBody(); 
		
	$id_trx_penjualan = $request_data['id_trx_penjualan'];

    $TrxPenjualan = $db->getTrxPenjualanByID($id_trx_penjualan);

    $response->write(json_encode($TrxPenjualan));

    return $response
    ->withHeader('Content-type', 'application/json')
    ->withStatus(200);  
	    
});

$app->post('/inserttrxpenjualan', function(Request $request, Response $response){
		
        $request_data = $request->getParsedBody(); 
		
		$id_trx_penjualan = $request_data['id_trx_penjualan'];
		$tgl_trx_input    = $request_data['tgl_trx_input'];
		$tagihan_akhir    = $request_data['tagihan_akhir'];
		$tagihan_awal     = $request_data['tagihan_awal'];
		$tax             = $request_data['tax'];
		$id_pelanggan     = $request_data['id_pelanggan'];
		$nama             = $request_data['nama'];	
		$jenis_tagihan    = $request_data['jenis_tagihan'];
		$id_user          = $request_data['id_user'];
		$total_diskon     = $request_data['total_diskon'];
		$no_meja          = $request_data['no_meja'];
		$komisi			  = $request_data['komisi'];	
		
		if($jenis_tagihan != 'Open bill'){
		$tgl_trx_bayar    = $request_data['tgl_trx_bayar'];
		$terbayar         = $request_data['terbayar'];
		$id_method_bayar  = $request_data['id_method_bayar'];
		}else{
		$tgl_trx_bayar    = null;
		$terbayar         = null;
		$id_method_bayar  = null;
		}
		
        $db = new TRX_PenjualanOperation; 
		
		$result = $db->InsertTransaksi($id_trx_penjualan,$tgl_trx_input,$tgl_trx_bayar,$tagihan_akhir,$tagihan_awal,$total_diskon,$tax,$terbayar,$komisi,$id_pelanggan,$nama,$no_meja,$id_method_bayar,$jenis_tagihan,$id_user);
		
        if($result == TRANSAKSI_BERHASI_DITAMBAH){
			$message = array(); 
            $message['error'] = false; 
            $message['message'] = 'Transaksi Berhasil Ditambah';

            $response->write(json_encode($message));

            return $response
                        ->withHeader('Content-type', 'application/json')
                        ->withStatus(201);

        }else if($result == TRANSAKSI_GAGAL_DITAMBAH){
			$message = array(); 
            $message['error'] = true; 
            $message['message'] = 'Transaksi Gagal Ditambah';

            $response->write(json_encode($message));

            return $response
                        ->withHeader('Content-type', 'application/json')
                        ->withStatus(422); 
						
		}else if($result == TRANSAKSI_SUDAH_ADA){
            $message = array(); 
            $message['error'] = true; 
            $message['message'] = 'Transaksi Sudah Ada';

            $response->write(json_encode($message));

            return $response
                        ->withHeader('Content-type', 'application/json')
                        ->withStatus(422);    

        }
});

$app->post('/updatetrxpenjualan', function(Request $request, Response $response, array $args){

        $request_data = $request->getParsedBody();      
		
		$id_trx_penjualan = $request_data['id_trx_penjualan'];
		$tgl_trx_bayar = $request_data['tgl_trx_bayar'];
		$tax             = $request_data['tax'];
		$terbayar = $request_data['terbayar'];
		$id_method_bayar = $request_data['id_method_bayar'];
		$jenis_tagihan = $request_data['jenis_tagihan'];
     

        $db = new TRX_PenjualanOperation; 
		
		
        if($db->updateTrxPenjualan($id_trx_penjualan,$tgl_trx_bayar,$tax,$terbayar,$id_method_bayar,$jenis_tagihan)){
            $response_data = array(); 
            $response_data['error'] = false; 
            $response_data['message'] = 'Transaksi Berhasil Diupdate';
            $TrxPenjualan = $db->getTrxPenjualanByID($id_trx_penjualan);
            $response_data['TrxPenjualan'] = $TrxPenjualan; 

            $response->write(json_encode($response_data));

            return $response
            ->withHeader('Content-type', 'application/json')
            ->withStatus(200);  
        
        }else{
            $response_data = array(); 
            $response_data['error'] = true; 
            $response_data['message'] = 'Transaksi Gagal Diupdate';
            $TrxPenjualan = $db->getTrxPenjualanByID($id_trx_penjualan);
            $response_data['TrxPenjualan'] = $TrxPenjualan; 

            $response->write(json_encode($response_data));

            return $response
            ->withHeader('Content-type', 'application/json')
            ->withStatus(200);  
              
        }
		
});

$app->post('/hapusTrxPenjualan', function(Request $request, Response $response, array $args){ 
		$request_data = $request->getParsedBody(); 
	
		$id_trx_penjualan = $request_data['id_trx_penjualan'];
		
		$db = new TRX_PenjualanOperation;
		
		if($db->hapusTrxPenjualan($id_trx_penjualan)){
			$response_data = array(); 
            $response_data['error'] = false; 
            $response_data['message'] = 'Trnsaksi yang Gagal Berhasil Dihapus';

            $response->write(json_encode($response_data));

            return $response
            ->withHeader('Content-type', 'application/json')
            ->withStatus(200);  
		}
});

$app->post('/tambahpesanan', function(Request $request, Response $response, array $args){

        $request_data = $request->getParsedBody();      
		
		$id_trx_penjualan = $request_data['id_trx_penjualan'];
		$tgl_trx_input = $request_data['tgl_trx_input'];
		$tagihan_akhir = $request_data['tagihan_akhir'];
		$tagihan_awal = $request_data['tagihan_awal'];
		$total_diskon = $request_data['total_diskon'];
		$tax = $request_data['tax'];

        $db = new TRX_PenjualanOperation; 
		
		
        if($db->TambahPesanan($id_trx_penjualan,$tgl_trx_input,$tagihan_akhir,$tagihan_awal,$total_diskon,$tax)){
            $response_data = array(); 
            $response_data['error'] = false; 
            $response_data['message'] = 'Pesanan Berhasil Ditambah';
            $TrxPenjualan = $db->getTrxPenjualanByID($id_trx_penjualan);
            $response_data['TrxPenjualan'] = $TrxPenjualan; 

            $response->write(json_encode($response_data));

            return $response
            ->withHeader('Content-type', 'application/json')
            ->withStatus(200);  
        
        }else{
            $response_data = array(); 
            $response_data['error'] = true; 
            $response_data['message'] = 'Pesanan Gagal Ditambah';
            $TrxPenjualan = $db->getTrxPenjualanByID($id_trx_penjualan);
            $response_data['TrxPenjualan'] = $TrxPenjualan; 

            $response->write(json_encode($response_data));

            return $response
            ->withHeader('Content-type', 'application/json')
            ->withStatus(200);  
              
        }
		
});

$app->run();