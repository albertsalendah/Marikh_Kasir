<?php


use \Psr\Http\Message\ServerRequestInterface as Request;
use \Psr\Http\Message\ResponseInterface as Response;

require '../vendor/autoload.php';

require '../includes/ProdukOperation.php';


$app = new \Slim\App([
    'settings'=>[
        'displayErrorDetails'=>true
    ]
]);

$app->post('/daftarproduk', function(Request $request, Response $response){
	
	$request_data = $request->getParsedBody(); 
	
	$id_kat_produk = $request_data['id_kat_produk'];
	
	$db = new ProdukOperation; 
    $product = $db->daftarproduk($id_kat_produk);

		$response->write(json_encode($product));

    return $response
    ->withHeader('Content-type', 'application/json')
    ->withStatus(200);
	    
});

$app->get('/allproduk', function(Request $request, Response $response){
	
	
	$db = new ProdukOperation; 
    $product = $db->allproduk();

		$response->write(json_encode($product));

    return $response
    ->withHeader('Content-type', 'application/json')
    ->withStatus(200);
	    
});

$app->post('/tambahproduk', function(Request $request, Response $response){
		
        $request_data = $request->getParsedBody(); 
		
		$nama_produk = $request_data['nama_produk'];
		$harga_produk = $request_data['harga_produk'];
		$qty_produk = $request_data['qty_produk'];
		$diskon_produk = $request_data['diskon_produk'];
        $id_kat_produk = $request_data['id_kat_produk'];
		$tgl_awal_diskon = $request_data['tgl_awal_diskon'];
		$tgl_akhir_diskon = $request_data['tgl_akhir_diskon'];
		$diskon_olfood = $request_data['diskon_olfood'];
		$tgl_awal_diskon_olfood = $request_data['tgl_awal_diskon_olfood'];
		$tgl_akhir_diskon_olfood = $request_data['tgl_akhir_diskon_olfood'];
		$gbr_produk = $request_data['gbr_produk'];
		
		if(empty($diskon_produk) && empty($diskon_olfood)&& empty($gbr_produk)){
			$diskon_produk = null;
			$tgl_awal_diskon = null;
			$tgl_akhir_diskon = null;
			$diskon_olfood = null;
			$tgl_awal_diskon_olfood = null;
			$tgl_akhir_diskon_olfood = null;
			$gbr_produk = null;
		}
		if(empty($diskon_produk) && empty($diskon_olfood)){
			$diskon_produk = null;
			$tgl_awal_diskon = null;
			$tgl_akhir_diskon = null;
			$diskon_olfood = null;
			$tgl_awal_diskon_olfood = null;
			$tgl_akhir_diskon_olfood = null;
		}
		if(empty($diskon_produk)){
			$diskon_produk = null;
			$tgl_awal_diskon = null;
			$tgl_akhir_diskon = null;
		}
		if(empty($diskon_olfood)){
			$diskon_olfood = null;
			$tgl_awal_diskon_olfood = null;
			$tgl_akhir_diskon_olfood = null;
		}
		if(empty($gbr_produk)){
			$gbr_produk = null;
		}
		$db = new ProdukOperation; 
        $result = $db->tambahproduct($nama_produk,$harga_produk,$qty_produk,$diskon_produk,$id_kat_produk,$tgl_awal_diskon,$tgl_akhir_diskon,$diskon_olfood,$tgl_awal_diskon_olfood,$tgl_akhir_diskon_olfood,$gbr_produk);
        if($result == PRODUK_BERHASIL_DITAMBAH){
			
            $message = array(); 
            $message['error'] = false; 
            $message['message'] = 'Produk Berhasil Ditambah';
            $response->write(json_encode($message));

            return $response
                        ->withHeader('Content-type', 'application/json')
                        ->withStatus(201);

        }else if($result == PRODUK_GAGAL_DITAMBAH){
			
            $message = array(); 
            $message['error'] = true; 
            $message['message'] = 'Produk Gagal Ditambah';

            $response->write(json_encode($message));

            return $response
                        ->withHeader('Content-type', 'application/json')
                        ->withStatus(422);    

        }else if ($result == PRODUK_SUDAH_ADA){
			$message = array(); 
            $message['error'] = true; 
            $message['message'] = 'Nama Produk Sudah Ada';

            $response->write(json_encode($message));

            return $response
                        ->withHeader('Content-type', 'application/json')
                        ->withStatus(422);
		}
});

$app->post('/getproduk', function(Request $request, Response $response, array $args){ 
$request_data = $request->getParsedBody(); 
	
	$id_produk = $request_data['id_produk'];
	
	$db = new ProdukOperation; 
    if($product = $db->getProdukByID($id_produk)){
		$response->write(json_encode($product));
		return $response
		->withHeader('Content-type', 'application/json')
		->withStatus(200);
	}
});

$app->post('/updateproduk', function(Request $request, Response $response, array $args){ 
		$request_data = $request->getParsedBody(); 
	
		$id_produk        = $request_data['id_produk'];
		$nama_produk      = $request_data['nama_produk'];
		$harga_produk     = $request_data['harga_produk'];
		$qty_produk       = $request_data['qty_produk'];
		$diskon_produk    = $request_data['diskon_produk'];
        $id_kat_produk    = $request_data['id_kat_produk'];
		$tgl_awal_diskon  = $request_data['tgl_awal_diskon'];
		$tgl_akhir_diskon = $request_data['tgl_akhir_diskon'];
		$diskon_olfood = $request_data['diskon_olfood'];
		$tgl_awal_diskon_olfood = $request_data['tgl_awal_diskon_olfood'];
		$tgl_akhir_diskon_olfood = $request_data['tgl_akhir_diskon_olfood'];
		$gbr_produk       = $request_data['gbr_produk'];
	
	$db = new ProdukOperation; 
	
	if(empty($diskon_produk) && empty($diskon_olfood)&& empty($gbr_produk)){
			$diskon_produk = null;
			$tgl_awal_diskon = null;
			$tgl_akhir_diskon = null;
			$diskon_olfood = null;
			$tgl_awal_diskon_olfood = null;
			$tgl_akhir_diskon_olfood = null;
			$gbr_produk = null;
		}
		if(empty($diskon_produk) && empty($diskon_olfood)){
			$diskon_produk = null;
			$tgl_awal_diskon = null;
			$tgl_akhir_diskon = null;
			$diskon_olfood = null;
			$tgl_awal_diskon_olfood = null;
			$tgl_akhir_diskon_olfood = null;
		}
		if(empty($diskon_produk)){
			$diskon_produk = null;
			$tgl_awal_diskon = null;
			$tgl_akhir_diskon = null;
		}
		if(empty($diskon_olfood)){
			$diskon_olfood = null;
			$tgl_awal_diskon_olfood = null;
			$tgl_akhir_diskon_olfood = null;
		}
		if(empty($gbr_produk)){
			$gbr_produk = null;
		}
	
	if($db->updateProduk($id_produk,$nama_produk,$harga_produk,$qty_produk,$diskon_produk,$id_kat_produk,$tgl_awal_diskon,$tgl_akhir_diskon,$diskon_olfood,$tgl_awal_diskon_olfood,$tgl_akhir_diskon_olfood,$gbr_produk)){
			
			$response_data = array(); 
            $response_data['error'] = false; 
            $response_data['message'] = 'Produk Berhasil Diupdate';
            $produk = $db->getProdukByID($id_produk);
            $response_data['produk'] = $produk; 

            $response->write(json_encode($response_data));

            return $response
            ->withHeader('Content-type', 'application/json')
            ->withStatus(200);  
		}
	
});

$app->post('/hapusproduk', function(Request $request, Response $response, array $args){ 
		$request_data = $request->getParsedBody(); 
	
		$id_produk = $request_data['id_produk'];
		$db = new ProdukOperation;
		if($db->hapusproduk($id_produk)){
			$response_data = array(); 
            $response_data['error'] = false; 
            $response_data['message'] = 'Produk Berhasil Dihapus';

            $response->write(json_encode($response_data));

            return $response
            ->withHeader('Content-type', 'application/json')
            ->withStatus(200);  
		}
});

$app->run();