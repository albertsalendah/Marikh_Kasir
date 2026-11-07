<?php


use \Psr\Http\Message\ServerRequestInterface as Request;
use \Psr\Http\Message\ResponseInterface as Response;

require '../vendor/autoload.php';

require '../includes/KategoriOperation.php';


$app = new \Slim\App([
    'settings'=>[
        'displayErrorDetails'=>true
    ]
]);

$app->get('/listkategori', function(Request $request, Response $response){

    $db = new KategoriOperation; 

    $kategori = $db->ListKategori();

    $response->write(json_encode($kategori));

    return $response
    ->withHeader('Content-type', 'application/json')
    ->withStatus(200);  

});

$app->post('/tambahkategori', function(Request $request, Response $response){
		
    if(!haveEmptyParameters(array('nama_kat_produk'), $request, $response)){

        $request_data = $request->getParsedBody(); 

        $nama_kat_produk = $request_data['nama_kat_produk'];

        $db = new KategoriOperation; 

        $result = $db->createKategori($nama_kat_produk);     
		
        if($result == KATEGORI_BERHASIL_DIBUAT){
			
            $message = array(); 
            $message['error'] = false; 
            $message['message'] = 'Kategori Berhasil Dibuat';

            $response->write(json_encode($message));

            return $response
                        ->withHeader('Content-type', 'application/json')
                        ->withStatus(201);

        }else if($result == KATEGORI_GAGAL_DIBUAT){

            $message = array(); 
            $message['error'] = true; 
            $message['message'] = 'Some error occurred';

            $response->write(json_encode($message));

            return $response
                        ->withHeader('Content-type', 'application/json')
                        ->withStatus(422);    

        }else if($result == KATEGORI_SUDAH_ADA){
            $message = array(); 
            $message['error'] = true; 
            $message['message'] = 'Kategori Sudah Ada';

            $response->write(json_encode($message));

            return $response
                        ->withHeader('Content-type', 'application/json')
                        ->withStatus(422);    
        }
    }
    return $response
        ->withHeader('Content-type', 'application/json')
        ->withStatus(422);    
});

$app->post('/updatekategori', function(Request $request, Response $response, array $args){

    if(!haveEmptyParameters(array('id_kat_produk','nama_kat_produk'), $request, $response)){

        $request_data = $request->getParsedBody();      
		
		$id_kat_produk = $request_data['id_kat_produk'];
		$nama_kat_produk = $request_data['nama_kat_produk'];
     

        $db = new KategoriOperation; 
		
		
        if($db->updateKategori($id_kat_produk,$nama_kat_produk)){
            $response_data = array(); 
            $response_data['error'] = false; 
            $response_data['message'] = 'Kategori Berhasil Diupdate';
            $kategori = $db->getKategoriByID($id_kat_produk);
            $response_data['kategori'] = $kategori; 

            $response->write(json_encode($response_data));

            return $response
            ->withHeader('Content-type', 'application/json')
            ->withStatus(200);  
        
        }else{
            $response_data = array(); 
            $response_data['error'] = true; 
            $response_data['message'] = 'Kategori Gagal Diupdate';
            $kategori = $db->getKategoriByID($id);
            $response_data['kategori'] = $kategori; 

            $response->write(json_encode($response_data));

            return $response
            ->withHeader('Content-type', 'application/json')
            ->withStatus(200);  
              
        }
		
    }
    
    return $response
    ->withHeader('Content-type', 'application/json')
    ->withStatus(200);  

});
$app->post('/hapuskategori', function(Request $request, Response $response){
	 if(!haveEmptyParameters(array('nama_kat_produk'), $request, $response)){

        $request_data = $request->getParsedBody(); 

        $nama_kat_produk = $request_data['nama_kat_produk'];

        $db = new KategoriOperation; 

			if($db->hapusKategori($nama_kat_produk)){
				$message = array(); 
            $message['error'] = false; 
            $message['message'] = 'Kategori Berhasil Dihapus';

            $response->write(json_encode($message));

            return $response
                        ->withHeader('Content-type', 'application/json')
                        ->withStatus(201);
			}else{
				$message = array(); 
				$message['error'] = false; 
				$message['message'] = 'Kategori Gagal Dihapus';

				$response->write(json_encode($message));

				return $response
                        ->withHeader('Content-type', 'application/json')
                        ->withStatus(422);
			}
		
	 }
	 return $response
        ->withHeader('Content-type', 'application/json')
        ->withStatus(422); 
});
function haveEmptyParameters($required_params, $request, $response){
    $error = false; 
    $error_params = '';
    $request_params = $request->getParsedBody(); 

    foreach($required_params as $param){
        if(!isset($request_params[$param]) || strlen($request_params[$param])<=0){
            $error = true; 
            $error_params .= $param . ', ';
        }
    }

    if($error){
        $error_detail = array();
        $error_detail['error'] = true; 
        $error_detail['message'] = 'Required parameters ' . substr($error_params, 0, -2) . ' are missing or empty';
        $response->write(json_encode($error_detail));
    }
    return $error; 
}

$app->run();