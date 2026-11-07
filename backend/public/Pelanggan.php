<?php


use \Psr\Http\Message\ServerRequestInterface as Request;
use \Psr\Http\Message\ResponseInterface as Response;

require '../vendor/autoload.php';

require '../includes/PelangganOperation.php';


$app = new \Slim\App([
    'settings'=>[
        'displayErrorDetails'=>true
    ]
]);

$app->post('/insertpelanggan', function(Request $request, Response $response){
		
        $request_data = $request->getParsedBody(); 
		
		$nama_pelanggan    = $request_data['nama_pelanggan'];
		$hp_pelanggan    = $request_data['hp_pelanggan'];
		$email_pelanggan     = $request_data['email_pelanggan'];
		$tgl_lahir_pelanggan  = $request_data['tgl_lahir_pelanggan'];
		$id_kat_pelanggan     = $request_data['id_kat_pelanggan'];
		
		if(empty($hp_pelanggan)){
			$hp_pelanggan = null;
		}
		if(empty($email_pelanggan)){
			$email_pelanggan = null;
		}
		if(empty($tgl_lahir_pelanggan)){
			$tgl_lahir_pelanggan = null;
		}
		
        $db = new PelangganOperation; 
		
		$result = $db->tambahPelanggan($nama_pelanggan,$hp_pelanggan,$email_pelanggan,$tgl_lahir_pelanggan,$id_kat_pelanggan);
		
        if($result){
			$message = array(); 
            $message['error'] = false; 
            $message['message'] = 'Data Berhasil Dinput';

            $response->write(json_encode($message));

            return $response
                        ->withHeader('Content-type', 'application/json')
                        ->withStatus(201);

        }else{
			$message = array(); 
            $message['error'] = true; 
            $message['message'] = 'Data Gagal Diinput';

            $response->write(json_encode($message));

            return $response
                        ->withHeader('Content-type', 'application/json')
                        ->withStatus(422); 
						
		}
});

$app->post('/updatePelanggan', function(Request $request, Response $response, array $args){ 
		$request_data = $request->getParsedBody(); 
	
		$id_pelanggan        = $request_data['id_pelanggan'];
		$nama_pelanggan    = $request_data['nama_pelanggan'];
		$hp_pelanggan    = $request_data['hp_pelanggan'];
		$email_pelanggan     = $request_data['email_pelanggan'];
		$tgl_lahir_pelanggan  = $request_data['tgl_lahir_pelanggan'];
		$id_kat_pelanggan     = $request_data['id_kat_pelanggan'];
	
	$db = new PelangganOperation; 
	
	if(empty($hp_pelanggan)){
			$hp_pelanggan = null;
		}
		if(empty($email_pelanggan)){
			$email_pelanggan = null;
		}
		if(empty($tgl_lahir_pelanggan)){
			$tgl_lahir_pelanggan = null;
		}		
	
	if($db->updatePelanggan($id_pelanggan,$nama_pelanggan,$hp_pelanggan,$email_pelanggan,$tgl_lahir_pelanggan,$id_kat_pelanggan)){
			
			$response_data = array(); 
            $response_data['error'] = false; 
            $response_data['message'] = 'Pelanggan Berhasil Diupdate';
            $pelanggan = $db->getPelangganByID($id_pelanggan);
            $response_data['pelanggan'] = $pelanggan; 

            $response->write(json_encode($response_data));

            return $response
            ->withHeader('Content-type', 'application/json')
            ->withStatus(200);  
		}
	
});

$app->post('/hapuspelanggan', function(Request $request, Response $response, array $args){ 
		$request_data = $request->getParsedBody(); 
	
		$id_pelanggan = $request_data['id_pelanggan'];
		$db = new PelangganOperation;
		if($db->hapusPelanggan($id_pelanggan)){
			$response_data = array(); 
            $response_data['error'] = false; 
            $response_data['message'] = 'Pelanggan Berhasil Dihapus';

            $response->write(json_encode($response_data));

            return $response
            ->withHeader('Content-type', 'application/json')
            ->withStatus(200);  
		}
});

$app->get('/daftarpelanggan', function(Request $request, Response $response){
	
	$db = new PelangganOperation; 

    $Pelanggan = $db->ListPelanggan();

    $response->write(json_encode($Pelanggan));

    return $response
    ->withHeader('Content-type', 'application/json')
    ->withStatus(200);  
	    
});

$app->get('/daftarkategoripelanggan', function(Request $request, Response $response){
	
	$db = new PelangganOperation; 

    $Pelanggan = $db->ListKategoriPelanggan();

    $response->write(json_encode($Pelanggan));

    return $response
    ->withHeader('Content-type', 'application/json')
    ->withStatus(200);  
	    
});

$app->post('/insertkategoripelanggan', function(Request $request, Response $response){
		
        $request_data = $request->getParsedBody(); 
		
		$nama_kat_pelanggan    = $request_data['nama_kat_pelanggan'];
			
        $db = new PelangganOperation; 
		
		$result = $db->tambahKategoriPelanggan($nama_kat_pelanggan);
		
        if($result){
			$message = array(); 
            $message['error'] = false; 
            $message['message'] = 'Kategori Pelanggan Berhasil Ditambahkan';

            $response->write(json_encode($message));

            return $response
                        ->withHeader('Content-type', 'application/json')
                        ->withStatus(201);

        }else{
			$message = array(); 
            $message['error'] = true; 
            $message['message'] = 'Kategori Pelanggan Gagal Ditambahkan';

            $response->write(json_encode($message));

            return $response
                        ->withHeader('Content-type', 'application/json')
                        ->withStatus(422); 
						
		}
});

$app->post('/updatekategoriPelanggan', function(Request $request, Response $response, array $args){ 
		$request_data = $request->getParsedBody(); 
	
		$id_kat_pelanggan        = $request_data['id_kat_pelanggan'];
		$nama_kat_pelanggan    = $request_data['nama_kat_pelanggan'];
	
	$db = new PelangganOperation; 
			
	
	if($db->updateKategoriPelanggan($id_kat_pelanggan,$nama_kat_pelanggan)){
			
			$response_data = array(); 
            $response_data['error'] = false; 
            $response_data['message'] = 'Kategori Pelanggan Berhasil Diupdate';
            $Kategoripelanggan = $db->getKategoriPelangganByID($id_kat_pelanggan);
            $response_data['kategoripelanggan'] = $Kategoripelanggan; 

            $response->write(json_encode($response_data));

            return $response
            ->withHeader('Content-type', 'application/json')
            ->withStatus(200);  
		}
	
});

$app->post('/hapuskategoripelanggan', function(Request $request, Response $response, array $args){ 
		$request_data = $request->getParsedBody(); 
	
		$id_kat_pelanggan = $request_data['id_kat_pelanggan'];
		$db = new PelangganOperation;
		if($db->hapusKategoriPelanggan($id_kat_pelanggan)){
			$response_data = array(); 
            $response_data['error'] = false; 
            $response_data['message'] = 'Kategori Pelanggan Berhasil Dihapus';

            $response->write(json_encode($response_data));

            return $response
            ->withHeader('Content-type', 'application/json')
            ->withStatus(200);  
		}
});

$app->run();