<?php


use \Psr\Http\Message\ServerRequestInterface as Request;
use \Psr\Http\Message\ResponseInterface as Response;

require '../vendor/autoload.php';

require '../includes/MethodPembayaranOperation.php';


$app = new \Slim\App([
    'settings'=>[
        'displayErrorDetails'=>true
    ]
]);

$app->get('/daftarmethodbayar', function(Request $request, Response $response){
	
	$db = new MethodPembayaranOperation; 

    $MethodBayar = $db->ListMethodPembayaran();

    $response->write(json_encode($MethodBayar));

    return $response
    ->withHeader('Content-type', 'application/json')
    ->withStatus(200);  
	    
});

$app->post('/insertmethodbayar', function(Request $request, Response $response){
		
        $request_data = $request->getParsedBody(); 
		
		$nama_method    = $request_data['nama_method'];
		$reguler    = $request_data['reguler'];
		$olfood    = $request_data['olfood'];
			
        $db = new MethodPembayaranOperation; 
		
		$result = $db->tambahMethodBayar($nama_method,$reguler,$olfood);
		
        if($result){
			$message = array(); 
            $message['error'] = false; 
            $message['message'] = 'Data Berhasil Ditambahkan';

            $response->write(json_encode($message));

            return $response
                        ->withHeader('Content-type', 'application/json')
                        ->withStatus(201);

        }else{
			$message = array(); 
            $message['error'] = true; 
            $message['message'] = 'Data Gagal Ditambahkan';

            $response->write(json_encode($message));

            return $response
                        ->withHeader('Content-type', 'application/json')
                        ->withStatus(422); 
						
		}
});

$app->post('/updateMethodBayar', function(Request $request, Response $response, array $args){ 
		$request_data = $request->getParsedBody(); 
	
		$id_method_bayar        = $request_data['id_method_bayar'];
		$nama_method    		= $request_data['nama_method'];
		$reguler    		= $request_data['reguler'];
		$olfood    		= $request_data['olfood'];
	
	$db = new MethodPembayaranOperation; 
			
	
	if($db->updateMethodBayar($id_method_bayar,$nama_method,$reguler,$olfood)){
			
			$response_data = array(); 
            $response_data['error'] = false; 
            $response_data['message'] = 'Data Berhasil Diupdate';
            $Kategoripelanggan = $db->getMethodBayarByID($id_method_bayar);
            $response_data['kategoripelanggan'] = $Kategoripelanggan; 

            $response->write(json_encode($response_data));

            return $response
            ->withHeader('Content-type', 'application/json')
            ->withStatus(200);  
		}
});


$app->run();