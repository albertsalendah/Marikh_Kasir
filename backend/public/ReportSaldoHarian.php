<?php


use \Psr\Http\Message\ServerRequestInterface as Request;
use \Psr\Http\Message\ResponseInterface as Response;

require '../vendor/autoload.php';

require '../includes/ReportSaldoHarianOperation.php';


$app = new \Slim\App([
    'settings'=>[
        'displayErrorDetails'=>true
    ]
]);

$app->get('/daftarReportSaldoHarian', function(Request $request, Response $response){
	
	$db = new ReportSaldoHarianOperation; 

    $SaldoHarian = $db->ListSaldoHarian();

    $response->write(json_encode($SaldoHarian));

    return $response
    ->withHeader('Content-type', 'application/json')
    ->withStatus(200);  
	    
});

$app->post('/getReportSaldoHarian', function(Request $request, Response $response){
	
	$db = new ReportSaldoHarianOperation; 
	
	$request_data = $request->getParsedBody(); 
		
	$tgl_report_sh = $request_data['tgl_report_sh'];

    $SaldoHarian = $db->getReportSaldoHarianByTgl($tgl_report_sh);

    $response->write(json_encode($SaldoHarian));

    return $response
    ->withHeader('Content-type', 'application/json')
    ->withStatus(200);  
	    
});

$app->post('/insertSaldoHarian', function(Request $request, Response $response){
		
        $request_data = $request->getParsedBody(); 
		
		$tgl_report_sh = $request_data['tgl_report_sh'];
		$saldo_awal = $request_data['saldo_awal'];
		
        $db = new ReportSaldoHarianOperation; 
		$result = $db->InsertSaldoHarian($tgl_report_sh,$saldo_awal);
		
        if($result == SALDO_BERHASIL_DITAMBAH && $db->InserTbReportOperational($saldo_awal)){
		
            $message = array(); 
            $message['error'] = false; 
            $message['message'] = 'Saldo Berhasil Ditambah';

            $response->write(json_encode($message));

            return $response
                        ->withHeader('Content-type', 'application/json')
                        ->withStatus(201);

        }else if($result == SALDO_GAGAL_DITAMBAH){
			$message = array(); 
            $message['error'] = true; 
            $message['message'] = 'Saldo Gagal Ditambah';

            $response->write(json_encode($message));

            return $response
                        ->withHeader('Content-type', 'application/json')
                        ->withStatus(422); 
						
		}else if($result == TGL_SUDAH_ADA){
            $message = array(); 
            $message['error'] = true; 
            $message['message'] = 'Anda Sudah Menanmbahkan Saldo Hari Ini';

            $response->write(json_encode($message));

            return $response
                        ->withHeader('Content-type', 'application/json')
                        ->withStatus(422);    

        }
});

$app->post('/updateReportSaldoHarian', function(Request $request, Response $response, array $args){

        $request_data = $request->getParsedBody();      
		
		$id_report_sh = $request_data['id_report_sh'];
		$tgl_report_sh = $request_data['tgl_report_sh'];
		$saldo_awal = $request_data['saldo_awal'];
		$saldo_akhir = $request_data['saldo_akhir'];
     

        $db = new ReportSaldoHarianOperation; 
		
		
        if($db->updateSaldoHarian($id_report_sh,$tgl_report_sh, $saldo_awal,$saldo_akhir)){
            $response_data = array(); 
            $response_data['error'] = false; 
            $response_data['message'] = 'Report Saldo Berhasil Diupdate';
            $ReportSaldoHarian = $db->getReportSaldoHarianByID($id_report_sh);
            $response_data['ReportSaldoHarian'] = $ReportSaldoHarian; 

            $response->write(json_encode($response_data));

            return $response
            ->withHeader('Content-type', 'application/json')
            ->withStatus(200);  
        
        }else{
            $response_data = array(); 
            $response_data['error'] = true; 
            $response_data['message'] = 'Report Saldo Gagal Diupdate';
            $ReportSaldoHarian = $db->getReportSaldoHarianByID($id_report_sh);
            $response_data['ReportSaldoHarian'] = $ReportSaldoHarian; 

            $response->write(json_encode($response_data));

            return $response
            ->withHeader('Content-type', 'application/json')
            ->withStatus(200);  
              
        }
		
});

$app->run();