<?php


use \Psr\Http\Message\ServerRequestInterface as Request;
use \Psr\Http\Message\ResponseInterface as Response;

require '../vendor/autoload.php';

require '../includes/DetailPenjualanOperation.php';


$app = new \Slim\App([
    'settings'=>[
        'displayErrorDetails'=>true
    ]
]);

$app->get('/daftarDetailPenjualan', function(Request $request, Response $response){
	
	$db = new DetailPenjualanOperation; 

    $DetailTrxPenjualan = $db->ListDetailTrxPenjualan();

    $response->write(json_encode($DetailTrxPenjualan));

    return $response
    ->withHeader('Content-type', 'application/json')
    ->withStatus(200);  
	    
});

$app->post('/insertdetailpenjualan', function(Request $request, Response $response){
		
        $request_data = $request->getParsedBody(); 
		
		$id_trx_detpenjualan = $request_data['id_trx_detpenjualan'];
		$id_trx_penjualan    = $request_data['id_trx_penjualan'];
		$qty_terjual         = $request_data['qty_terjual'];
		$harga_terjual       = $request_data['harga_terjual'];
		$diskon_terjual      = $request_data['diskon_terjual'];
		$status_pesanan      = $request_data['status_pesanan'];
		$id_produk           = $request_data['id_produk'];
		$jenis_transaksi     = $request_data['jenis_transaksi'];
		$deskripsi			 = $request_data['deskripsi'];
		$catatan_tambahan	 = $request_data['catatan_tambahan'];
	
        $db = new DetailPenjualanOperation; 
		
		$result = $db->InsertDetailPenjualan($id_trx_detpenjualan,$id_trx_penjualan,$qty_terjual,$harga_terjual,$diskon_terjual,$status_pesanan,$id_produk,$jenis_transaksi,$deskripsi,$catatan_tambahan);
		
        if($result == DETAIL_TRANSAKSI_BERHASIL_DITAMBAH){
			
            $message = array(); 
            $message['error'] = false; 
            $message['message'] = 'Detail Transaksi Berhasil Ditambah';

            $response->write(json_encode($message));

            return $response
                        ->withHeader('Content-type', 'application/json')
                        ->withStatus(201);

        }else if($result == DETAIL_TRANSAKSI_GAGAL_DITAMBAH){
			$message = array(); 
            $message['error'] = true; 
		    $message['message'] = 'Detail Transaksi Gagal Ditambah';

            $response->write(json_encode($message));

            return $response
                        ->withHeader('Content-type', 'application/json')
                        ->withStatus(422); 
						
		}else if($result == DETAIL_TRANSAKSI_SUDAH_ADA){
            $message = array(); 
            $message['error'] = true; 
            $message['message'] = 'Detail Transaksi Sudah Ada';

            $response->write(json_encode($message));

            return $response
                        ->withHeader('Content-type', 'application/json')
                        ->withStatus(422);    

        }
});

$app->post('/getdetailpenjualan', function(Request $request, Response $response){
	
	$db = new DetailPenjualanOperation; 
	
	$request_data = $request->getParsedBody(); 
		
	$id_trx_penjualan = $request_data['id_trx_penjualan'];

    $DetailPenjualan = $db->getDetailTrxPenjualanByID($id_trx_penjualan);

    $response->write(json_encode($DetailPenjualan));

    return $response
    ->withHeader('Content-type', 'application/json')
    ->withStatus(200);  
	    
});


$app->run();