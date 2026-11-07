<?php


use \Psr\Http\Message\ServerRequestInterface as Request;
use \Psr\Http\Message\ResponseInterface as Response;

require '../vendor/autoload.php';

require '../includes/PengeluaranOperation.php';


$app = new \Slim\App([
    'settings'=>[
        'displayErrorDetails'=>true
    ]
]);

$app->get('/allpengeluaran', function(Request $request, Response $response){
	
	
	$db = new PengeluaranOperation; 
    $pengeluaran = $db->allPengeluaran();

		$response->write(json_encode($pengeluaran));

    return $response
    ->withHeader('Content-type', 'application/json')
    ->withStatus(200);
	    
});

$app->post('/insertpengeluaran', function(Request $request, Response $response){
		
        $request_data = $request->getParsedBody(); 
		
		$id_kat_pengeluaran    = $request_data['id_kat_pengeluaran'];
		$nama_pengeluaran    = $request_data['nama_pengeluaran'];
		$tgl_pengeluaran     = $request_data['tgl_pengeluaran'];
		$nominal_pengeluaran  = $request_data['nominal_pengeluaran'];
		$qty_pengeluaran     = $request_data['qty_pengeluaran'];
		$total_pengeluaran             = $request_data['total_pengeluaran'];	
		$jenis_post_pengeluaran    = $request_data['jenis_post_pengeluaran'];
		
		
        $db = new PengeluaranOperation; 
		
		$result = $db->tambahPengeluaran($id_kat_pengeluaran,$nama_pengeluaran,$tgl_pengeluaran,$nominal_pengeluaran,$qty_pengeluaran,$total_pengeluaran,$jenis_post_pengeluaran);
		
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

$app->post('/updatePengeluaran', function(Request $request, Response $response, array $args){ 
		$request_data = $request->getParsedBody(); 
	
		$id_trx_pengeluaran        = $request_data['id_trx_pengeluaran'];
		$id_kat_pengeluaran    = $request_data['id_kat_pengeluaran'];
		$nama_pengeluaran    = $request_data['nama_pengeluaran'];
		$tgl_pengeluaran     = $request_data['tgl_pengeluaran'];
		$nominal_pengeluaran  = $request_data['nominal_pengeluaran'];
		$qty_pengeluaran     = $request_data['qty_pengeluaran'];
		$total_pengeluaran     = $request_data['total_pengeluaran'];
		$jenis_post_pengeluaran     = $request_data['jenis_post_pengeluaran'];
	
	$db = new PengeluaranOperation; 
	
	if($db->updatePengeluaran($id_trx_pengeluaran,$id_kat_pengeluaran,$nama_pengeluaran,$tgl_pengeluaran,$nominal_pengeluaran,$qty_pengeluaran,$total_pengeluaran,$jenis_post_pengeluaran)){
			
			$response_data = array(); 
            $response_data['error'] = false; 
            $response_data['message'] = 'Data Berhasil Diupdate';
            $pengeluaran = $db->getPengeluaranByID($id_trx_pengeluaran);
            $response_data['pengeluaran'] = $pengeluaran; 

            $response->write(json_encode($response_data));

            return $response
            ->withHeader('Content-type', 'application/json')
            ->withStatus(200);  
		}
	
});

$app->get('/allkategoripengeluaran', function(Request $request, Response $response){
	
	
	$db = new PengeluaranOperation; 
    $kategoripengeluaran = $db->allKategoriPengeluaran();

		$response->write(json_encode($kategoripengeluaran));

    return $response
    ->withHeader('Content-type', 'application/json')
    ->withStatus(200);
	    
});

$app->post('/getkategoripengeluaranbyid', function(Request $request, Response $response, array $args){ 
$request_data = $request->getParsedBody(); 
	
	$id_kat_pengeluaran = $request_data['id_kat_pengeluaran'];
	
	$db = new PengeluaranOperation; 
    if($kat_pengeluaran = $db->getKategoriPengeluaranByID($id_kat_pengeluaran)){
		$response->write(json_encode($kat_pengeluaran));
		return $response
		->withHeader('Content-type', 'application/json')
		->withStatus(200);
	}
});

$app->post('/insertkategoripengeluaran', function(Request $request, Response $response){
		
        $request_data = $request->getParsedBody(); 
		
		$nama_kat_pengeluaran = $request_data['nama_kat_pengeluaran'];
		
		
        $db = new PengeluaranOperation; 
		
		$result = $db->tambahKategoriPengeluaran($nama_kat_pengeluaran);
		
        if($result){
			$message = array(); 
            $message['error'] = false; 
            $message['message'] = 'Kategori Berhasil Dinput';

            $response->write(json_encode($message));

            return $response
                        ->withHeader('Content-type', 'application/json')
                        ->withStatus(201);

        }else{
			$message = array(); 
            $message['error'] = true; 
            $message['message'] = 'Kategori Gagal Diinput';

            $response->write(json_encode($message));

            return $response
                        ->withHeader('Content-type', 'application/json')
                        ->withStatus(422); 
						
		}
});

$app->post('/updatekategoriPengeluaran', function(Request $request, Response $response, array $args){ 
		$request_data = $request->getParsedBody(); 
	
		$id_kat_pengeluaran        = $request_data['id_kat_pengeluaran'];
		$nama_kat_pengeluaran    = $request_data['nama_kat_pengeluaran'];
	
	$db = new PengeluaranOperation; 
			
	
	if($db->updateKategoriPengeluaran($id_kat_pengeluaran,$nama_kat_pengeluaran)){
			
			$response_data = array(); 
            $response_data['error'] = false; 
            $response_data['message'] = 'Kategori Berhasil Diupdate';
            $Kategoripengeluaran = $db->getKategoriPengeluaranByID($id_kat_pengeluaran);
            $response_data['kategoripengeluaran'] = $Kategoripengeluaran; 

            $response->write(json_encode($response_data));

            return $response
            ->withHeader('Content-type', 'application/json')
            ->withStatus(200);  
		}
	
});

$app->run();