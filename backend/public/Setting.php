<?php


use \Psr\Http\Message\ServerRequestInterface as Request;
use \Psr\Http\Message\ResponseInterface as Response;

require '../vendor/autoload.php';

require '../includes/SettingOperation.php';


$app = new \Slim\App([
    'settings'=>[
        'displayErrorDetails'=>true
    ]
]);

$app->get('/settings', function(Request $request, Response $response){
	
	$db = new SettingOperation; 

    $setting = $db->ListSetting();

    $response->write(json_encode($setting));

    return $response
    ->withHeader('Content-type', 'application/json')
    ->withStatus(200);  
	    
});

$app->post('/insertSetting', function(Request $request, Response $response){
		
        $request_data = $request->getParsedBody(); 
		
		$modal_awal     = $request_data['modal_awal'];
		$tax    		= $request_data['tax'];
		$gofoodph       = $request_data['gofoodph'];
		$gofoodec  		= $request_data['gofoodec'];
		$gofoodkomisi   = $request_data['gofoodkomisi'];
		$grabfoodph     = $request_data['grabfoodph'];
		$grabfoodec     = $request_data['grabfoodec'];
		$grabfoodkomisi = $request_data['grabfoodkomisi'];
		
		
        $db = new SettingOperation; 
		
		$result = $db->tambahSetting($modal_awal,$tax,$gofoodph,$gofoodec,$gofoodkomisi,$grabfoodph,$grabfoodec,$grabfoodkomisi);
		
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

$app->post('/updateGofood', function(Request $request, Response $response, array $args){ 
		$request_data = $request->getParsedBody(); 
	
		$id        		 = $request_data['id'];
		$gofoodph    	 = $request_data['gofoodph'];
		$gofoodec    	 = $request_data['gofoodec'];
		$gofoodkomisi    = $request_data['gofoodkomisi'];
	
	$db = new SettingOperation; 
			
	
	if($db->updateGofood($id,$gofoodph,$gofoodec,$gofoodkomisi)){
			
			$response_data = array(); 
            $response_data['error'] = false; 
            $response_data['message'] = 'Setting Gofood Berhasil Diupdate';

            $response->write(json_encode($response_data));

            return $response
            ->withHeader('Content-type', 'application/json')
            ->withStatus(200);  
		}
	
});

$app->post('/updateGrabfood', function(Request $request, Response $response, array $args){ 
		$request_data = $request->getParsedBody(); 
	
		$id        		 = $request_data['id'];
		$grabfoodph    	 = $request_data['grabfoodph'];
		$grabfoodec    	 = $request_data['grabfoodec'];
		$grabfoodkomisi    = $request_data['grabfoodkomisi'];
	
	$db = new SettingOperation; 
			
	
	if($db->updateGrabfood($id,$grabfoodph,$grabfoodec,$grabfoodkomisi)){
			
			$response_data = array(); 
            $response_data['error'] = false; 
            $response_data['message'] = 'Setting Grabfood Berhasil Diupdate';

            $response->write(json_encode($response_data));

            return $response
            ->withHeader('Content-type', 'application/json')
            ->withStatus(200);  
		}
	
});

$app->post('/updateModalAwal', function(Request $request, Response $response, array $args){ 
		$request_data = $request->getParsedBody(); 
	
		$id        		 = $request_data['id'];
		$modal_awal    	 = $request_data['modal_awal'];
		
	$db = new SettingOperation; 
			
	
	if($db->updateModalAwal($id,$modal_awal)){
			
			$response_data = array(); 
            $response_data['error'] = false; 
            $response_data['message'] = 'Setting Modal Awal Berhasil Diupdate';

            $response->write(json_encode($response_data));

            return $response
            ->withHeader('Content-type', 'application/json')
            ->withStatus(200);  
		}
	
});

$app->post('/updateTax', function(Request $request, Response $response, array $args){ 
		$request_data = $request->getParsedBody(); 
	
		$id        		 = $request_data['id'];
		$tax    	 = $request_data['tax'];
		
	$db = new SettingOperation; 
			
	
	if($db->updateTax($id,$tax)){
			
			$response_data = array(); 
            $response_data['error'] = false; 
            $response_data['message'] = 'Setting Tax Berhasil Diupdate';

            $response->write(json_encode($response_data));

            return $response
            ->withHeader('Content-type', 'application/json')
            ->withStatus(200);  
		}
	
});

$app->get('/listreportoperational', function(Request $request, Response $response){
	
	$db = new SettingOperation; 

    $setting = $db->ListReportOperational();

    $response->write(json_encode($setting));

    return $response
    ->withHeader('Content-type', 'application/json')
    ->withStatus(200);  
	    
});

$app->post('/insertreportoperational', function(Request $request, Response $response){
		
        $request_data = $request->getParsedBody(); 
		
		$id_report_all     = $request_data['id_report_all'];
		$tgl_trx_operational = $request_data['tgl_trx_operational'];
		$keterangan       = $request_data['keterangan'];
		$kredit_op  		= $request_data['kredit_op'];
		$debit_op   = $request_data['debit_op'];
		
		
        $db = new SettingOperation; 
		
		$result = $db->tambah_report_operational($id_report_all,$tgl_trx_operational,$keterangan,$kredit_op,$debit_op);
		
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


$app->run();