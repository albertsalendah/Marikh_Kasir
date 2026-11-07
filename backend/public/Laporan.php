<?php


use \Psr\Http\Message\ServerRequestInterface as Request;
use \Psr\Http\Message\ResponseInterface as Response;

require '../vendor/autoload.php';

require '../includes/LaporanOperation.php';


$app = new \Slim\App([
    'settings'=>[
        'displayErrorDetails'=>true
    ]
]);

$app->post('/insertLaporan', function(Request $request, Response $response, array $args){ 

	$db = new LaporanOperation; 
	
	$result = $db->updateSaldoAkhir();
    
	if($result){
	    $db->InsertLaporanOperational();
	    $db->InsertLaporanVirtualACC();
	   
		$response->write(json_encode($db->InsertLaporanOperational()));
		return $response
			->withHeader('Content-type', 'application/json')
			->withStatus(200); 
	}				
});

$app->post('/updateSaldoAkhir', function(Request $request, Response $response, array $args){ 
    
	$request_data = $request->getParsedBody();
	$idmethodbayar = $request_data['idmethodbayar'];
	
	$db = new LaporanOperation; 
	
	$result = $db->getNamaMethodBayar($idmethodbayar);
	if($result){
		$response->write(json_encode($result));
		return $response
			->withHeader('Content-type', 'application/json')
			->withStatus(200); 
	}
	 /*if($saldo_akhir){
			if($db -> getSaldoAkhir() != null){
			if($db -> getToday() > $db -> getYesterday()){
				$tgl_trx_operational = $db -> getToday();						
				$number = 1;
				$number = sprintf('%03d',$number);			
				$id_report_all = "OP{$tgl_trx_operational}{$number}";
				$keterangan = 'Kas Harian';
				$kas_harian = $db -> getSaldoAkhir();
				$debit_op = 0;			
				$KasHarian = $db->InserTbReportOperational($id_report_all,$tgl_trx_operational,$keterangan,$kas_harian,$debit_op);
				
				$tgl_trx_operational2 = $db -> getToday();						
					$number2 = 2;
					$number2 = sprintf('%03d',$number2);			
					$id_report_all2 = "OP{$tgl_trx_operational2}{$number2}";
					$keterangan2 = 'Pengeluaran Operational';
					$pOperational = $db -> getTotalPengeluaranOP();
					$debit_op2 = 0;
					$pengeluaranoperational = $db->InserTbReportOperational($id_report_all2,$tgl_trx_operational2,$keterangan2,$pOperational,$debit_op2);
				$tgl_trx_operational3 = $db -> getToday();						
						$number3 = 3;
						$number3 = sprintf('%03d',$number3);			
						$id_report_all3 = "OP{$tgl_trx_operational3}{$number3}";
						$keterangan3 = 'Pendapatan ke Virtual ACC';
						$virtualAcc = $db -> getPendapatanVirtualACC();
						$debit_op3 = 0;
						$pendapatankeVirtualACC = $db->InserTbReportOperational($id_report_all3,$tgl_trx_operational3,$keterangan3,$virtualAcc,$debit_op3);	
				if($KasHarian && $pengeluaranoperational && $pendapatankeVirtualACC){									
					$message = array(); 
					$message['error'] = false; 
					$message['message'] = 'Kategori Berhasil Dinput';

					$response->write(json_encode($message));

						return $response
							->withHeader('Content-type', 'application/json')
							->withStatus(201);					
				}else{
					return false;
				}				
			}else{
				return false;
			}			
		}else{
			return false;
		}
        }else{
			$message = array(); 
            $message['error'] = true; 
            $message['message'] = 'Kategori Gagal Diinput';

            $response->write(json_encode($message));

            return $response
                        ->withHeader('Content-type', 'application/json')
                        ->withStatus(422); 
						
		}
	*/
			
});

$app->post('/laporanBukuBesar', function(Request $request, Response $response, array $args){ 
    $request_data = $request->getParsedBody(); 
    
    $tglawal = $request_data['tglawal'];
	$tglakhir    = $request_data['tglakhir'];

	$db = new LaporanOperation; 
	
	$result = $db->laporanBukuBesar($tglawal,$tglakhir);
	if($result){
		$response->write(json_encode($result));
		return $response
			->withHeader('Content-type', 'application/json')
			->withStatus(200); 
	}				
});

$app->post('/laporanPenjualan', function(Request $request, Response $response, array $args){ 
    $request_data = $request->getParsedBody(); 
    
    $tglawal = $request_data['tglawal'];
	$tglakhir    = $request_data['tglakhir'];
	$method_bayar = $request_data['method_bayar'];

	$db = new LaporanOperation; 
	
	$result = $db->laporan_penjualan($tglawal,$tglakhir,$method_bayar);
	if($result){
		$response->write(json_encode($result));
		return $response
			->withHeader('Content-type', 'application/json')
			->withStatus(200); 
	}				
});

$app->post('/withdrawlAcc', function(Request $request, Response $response, array $args){ 
    $request_data = $request->getParsedBody(); 
    
    $idmethodbayar = $request_data['idmethodbayar'];
    $tgl_trx_virtual_acc = $request_data['tgl_trx_virtual_acc'];
	$saldo    = $request_data['saldo'];

	$db = new LaporanOperation; 
	
	$result = $db->withdrawl_acc($idmethodbayar,$tgl_trx_virtual_acc,$saldo);
	if($result){
	    $in_report_op = $db->withdrawl_report_operational($idmethodbayar,$tgl_trx_virtual_acc,$saldo);
	    if($in_report_op){
	        $message = array(); 
        $message['error'] = false; 
		$message['message'] = 'Data Berhasil Ditambah';
		$response->write(json_encode($message));
		return $response
			->withHeader('Content-type', 'application/json')
			->withStatus(200); 
	    }else{
	    $message = array(); 
        $message['error'] = true; 
		$message['message'] = 'Data Report Operational Gagal Ditambah';
		$response->write(json_encode($message));
        return $response
                    ->withHeader('Content-type', 'application/json')
                    ->withStatus(422);
	    }
	}else{
	    $message = array(); 
        $message['error'] = true; 
		$message['message'] = 'Data Gagal Ditambah';
		$response->write(json_encode($message));
        return $response
                    ->withHeader('Content-type', 'application/json')
                    ->withStatus(422); 
	}				
});

$app->post('/saldo_virtual_acc', function(Request $request, Response $response, array $args){ 
    $request_data = $request->getParsedBody(); 
    
	$method_bayar = $request_data['method_bayar'];

	$db = new LaporanOperation; 
	
	$result = $db->saldo_virtual_acc($method_bayar);
	if($result){
		$response->write(json_encode($result));
		return $response
			->withHeader('Content-type', 'application/json')
			->withStatus(200); 
	}				
});

$app->post('/listAcc', function(Request $request, Response $response, array $args){ 
    
	$db = new LaporanOperation; 
	
	$result = $db->list_virtual_acc();
	if($result){
		$response->write(json_encode($result));
		return $response
			->withHeader('Content-type', 'application/json')
			->withStatus(200); 
	}				
});

$app->run();