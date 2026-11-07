<?php
use \Psr\Http\Message\ServerRequestInterface as Request;
use \Psr\Http\Message\ResponseInterface as Response;

require '../vendor/autoload.php';

require '../includes/DeskripsiOperation.php';


$app = new \Slim\App([
    'settings'=>[
        'displayErrorDetails'=>true
    ]
]);

$app->post('/tambahdeskripsiproduk', function(Request $request, Response $response){
	
	$request_data = $request->getParsedBody(); 
	
	$nama_deskripsi = $request_data['nama_deskripsi'];
	$id_kat_produk = $request_data['id_kat_produk'];
	
	$db = new DeskripsiOperation; 
    if($deskripsi = $db->tambah_deskripsi_produk($nama_deskripsi,$id_kat_produk)){
	
			$message = array(); 
            $message['error'] = false; 
            $message['message'] = 'Deskripsi Berhasil Ditambah';

            $response->write(json_encode($message));

    return $response
    ->withHeader('Content-type', 'application/json')
    ->withStatus(200);
	}

    return $response
    ->withHeader('Content-type', 'application/json')
    ->withStatus(200);
});

$app->post('/daftardeskripsiproduk', function(Request $request, Response $response){
	
	$request_data = $request->getParsedBody(); 
	
	$id_kat_produk = $request_data['id_kat_produk'];
	
	$db = new DeskripsiOperation; 
    $deskripsi = $db->getdaftardeskripsi($id_kat_produk);

		$response->write(json_encode($deskripsi));

    return $response
    ->withHeader('Content-type', 'application/json')
    ->withStatus(200);
	    
});

$app->post('/updatedeskripsiproduk', function(Request $request, Response $response, array $args){ 
		$request_data = $request->getParsedBody(); 
	
		$id_deskripsi        = $request_data['id_deskripsi'];
		$nama_deskripsi       = $request_data['nama_deskripsi'];
	
	$db = new DeskripsiOperation; 
	if($db->update_deskripsi_produk($id_deskripsi,$nama_deskripsi)){
			
			$response_data = array(); 
            $response_data['error'] = false; 
            $response_data['message'] = 'Deskripsi Berhasil Diupdate';
            $Deskripsi = $db->getDeskripsiByID($id_deskripsi);
            $response_data['Deskripsi'] = $Deskripsi; 

            $response->write(json_encode($response_data));

            return $response
            ->withHeader('Content-type', 'application/json')
            ->withStatus(200);  
		}
	
});

$app->post('/hapusdeskripsiproduk', function(Request $request, Response $response, array $args){ 
		$request_data = $request->getParsedBody(); 
	
		$id_deskripsi = $request_data['id_deskripsi'];
		
		$db = new DeskripsiOperation;
		
		if($db->hapusdeskripsi($id_deskripsi)){
			$response_data = array(); 
            $response_data['error'] = false; 
            $response_data['message'] = 'Deskripsi Berhasil Dihapus';

            $response->write(json_encode($response_data));

            return $response
            ->withHeader('Content-type', 'application/json')
            ->withStatus(200);  
		}
});


$app->run();