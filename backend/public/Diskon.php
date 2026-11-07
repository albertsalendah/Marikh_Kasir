<?php


use \Psr\Http\Message\ServerRequestInterface as Request;
use \Psr\Http\Message\ResponseInterface as Response;

require '../vendor/autoload.php';

require '../includes/DiskonOperation.php';


$app = new \Slim\App([
    'settings'=>[
        'displayErrorDetails'=>true
    ]
]);

$app->get('/daftardiskon', function(Request $request, Response $response){
	
	$db = new DiskonOperation; 

    $Diskon = $db->ListDiskon();

    $response->write(json_encode($Diskon));

    return $response
    ->withHeader('Content-type', 'application/json')
    ->withStatus(200);  
	    
});

$app->post('/insertDiskon', function(Request $request, Response $response){
		
        $request_data = $request->getParsedBody(); 
		
		$deskripsi_disc    = $request_data['deskripsi_disc'];
		$nominal_disc    = $request_data['nominal_disc'];
			
        $db = new DiskonOperation; 
		
		$result = $db->tambahDiskon($deskripsi_disc,$nominal_disc);
		
        if($result){
			$message = array(); 
            $message['error'] = false; 
            $message['message'] = 'Diskon Berhasil Ditambahkan';

            $response->write(json_encode($message));

            return $response
                        ->withHeader('Content-type', 'application/json')
                        ->withStatus(201);

        }else{
			$message = array(); 
            $message['error'] = true; 
            $message['message'] = 'Diskon Gagal Ditambahkan';

            $response->write(json_encode($message));

            return $response
                        ->withHeader('Content-type', 'application/json')
                        ->withStatus(422); 
						
		}
});

$app->post('/updateDiskon', function(Request $request, Response $response, array $args){ 
		$request_data = $request->getParsedBody(); 
	
		$id_disc        = $request_data['id_disc'];
		$deskripsi_disc    = $request_data['deskripsi_disc'];
		$nominal_disc   = $request_data['nominal_disc'];
	
	$db = new DiskonOperation; 
			
	
	if($db->updateDiskon($id_disc,$deskripsi_disc,$nominal_disc)){
			
			$response_data = array(); 
            $response_data['error'] = false; 
            $response_data['message'] = 'Diskon Berhasil Diupdate';
            $diskon = $db->getDiskonByID($id_disc);
            $response_data['diskon'] = $diskon; 

            $response->write(json_encode($response_data));

            return $response
            ->withHeader('Content-type', 'application/json')
            ->withStatus(200);  
		}
	
});

$app->run();