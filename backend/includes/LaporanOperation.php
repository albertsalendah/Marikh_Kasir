<?php

	class LaporanOperation{
		function __construct(){
            require_once dirname(__FILE__) . '/DbConnect.php';
            $db = new DbConnect; 
            $this->con = $db->connect(); 
        }
	//-----------------------------------------------GET SALDO AKHIR----------------------------------------------------------//	
		public function getSaldoAwal(){				
			$stmt = $this->con->prepare("SELECT saldo_awal FROM tb_report_saldo_harian WHERE tgl_report_sh = CURDATE()");
			$stmt->execute(); 
			$stmt->bind_result($saldo_awal);
            $stmt->fetch();						
			return $saldo_awal;
		}
		
		public function getTotalPengeluaranHarian(){
			$stmt = $this->con->prepare("SELECT sum(total_pengeluaran) FROM tb_trx_pengeluaran WHERE DATE_FORMAT(tgl_pengeluaran,'%Y-%m-%d') = DATE_FORMAT(NOW(),'%Y-%m-%d') and jenis_post_pengeluaran = 'kas harian'");
			$stmt->execute(); 
			$stmt->bind_result($total_pengeluaran_harian);
            $stmt->fetch();			
			return $total_pengeluaran_harian;
		}
		
		public function getPenjualanHarianCash(){
			$stmt = $this->con->prepare("SELECT sum(tagihan_akhir-komisi) FROM tb_trx_penjualan WHERE id_method_bayar = 1 and jenis_tagihan = 'Lunas' and DATE_FORMAT(tgl_trx_input,'%Y-%m-%d') = DATE_FORMAT(NOW(),'%Y-%m-%d');");
			$stmt->execute(); 
			$stmt->bind_result($tagihan_akhir);
            $stmt->fetch();			
			return $tagihan_akhir;
		}
		
		public function updateSaldoAkhir(){
			if($this->getSaldoAwal() != null){
				if($this->getTotalPengeluaranHarian() != null){
					if($this->getPenjualanHarianCash() != null){
						$saldo_awal = $this->getSaldoAwal();
						$total_pengeluaran_harian = $this->getTotalPengeluaranHarian();
						$penjualan_harian_cash = $this->getPenjualanHarianCash();
						$saldo_akhir = ($saldo_awal+$penjualan_harian_cash)-$total_pengeluaran_harian;
			
						$stmt = $this->con->prepare("UPDATE tb_report_saldo_harian SET saldo_akhir = ? WHERE tgl_report_sh = DATE_FORMAT(NOW(),'%Y-%m-%d')");
						$stmt->bind_param("s", $saldo_akhir);						
						if($stmt->execute()){
							return true;
						}else{
							return false;
						}							
					}else{
						return false;
					}
				}else{
					$message = array(); 
					$message['error'] = true; 
					$message['message'] = 'Pengeluaran Hari Ini Belum diInput';
					return $message;
				}
			}else{
				$message = array(); 
				$message['error'] = true; 
				$message['message'] = 'Anda Belum Menginput Saldo Awal Untuk Hari Ini';
				return $message;
			}
		}
	//-----------------------------------------------------------------------------------------------------------------------//	
	
	//-----------------------------------------------REPORT OPERATIONAL----------------------------------------------------------//
	
	public function getSaldoAkhir(){				
			$stmt = $this->con->prepare("SELECT saldo_akhir FROM tb_report_saldo_harian WHERE tgl_report_sh = CURDATE()");
			$stmt->execute(); 
			$stmt->bind_result($saldo_akhir);
            $stmt->fetch();
						
			return $saldo_akhir;
		}
	
	
	public function getTotalPengeluaranOP(){
		$stmt = $this->con->prepare("SELECT sum(total_pengeluaran) FROM tb_trx_pengeluaran WHERE DATE_FORMAT(tgl_pengeluaran,'%Y-%m-%d') = DATE_FORMAT(NOW(),'%Y-%m-%d') and jenis_post_pengeluaran = 'kas operational'");
			$stmt->execute(); 
			$stmt->bind_result($total_pengeluaran_op);
            $stmt->fetch();
			
			return $total_pengeluaran_op;
	}
	
	public function getPendapatanVirtualACC(){
		$stmt = $this->con->prepare("SELECT sum(tagihan_akhir-komisi) FROM tb_trx_penjualan WHERE id_method_bayar != 1 and jenis_tagihan = 'Lunas' and DATE_FORMAT(tgl_trx_input,'%Y-%m-%d') = DATE_FORMAT(NOW(),'%Y-%m-%d');");
			$stmt->execute(); 
			$stmt->bind_result($pendapatan_ke_virtual_acc);
            $stmt->fetch();
			
			return $pendapatan_ke_virtual_acc;
	}
	
	public function InserTbReportOperational($id_report_all,$tgl_trx_operational,$keterangan,$kas_harian,$debit_op){
		if(!$this -> cekInputTbReportOperational($tgl_trx_operational,$keterangan)){			
			$stmt = $this->con->prepare("INSERT INTO tb_report_operational(id_report_all,tgl_trx_operational,keterangan,kredit_op,debit_op) VALUES (?,?,?,?,?)");
			$stmt->bind_param("sssss",$id_report_all,$tgl_trx_operational,$keterangan,$kas_harian,$debit_op);
			if($stmt->execute()){				
				return true;
			}else{
			return false;
			}		
		}		
	}
	
	public function cekInputTbReportOperational($tgl_trx_operational,$keterangan){
		$stmt = $this->con->prepare("SELECT tgl_trx_operational,keterangan FROM tb_report_operational WHERE tgl_trx_operational = ? and keterangan = ?");
         $stmt->bind_param("ss",$tgl_trx_operational,$keterangan);
         $stmt->execute(); 
         $stmt->store_result(); 
		 if($stmt->num_rows > 0){
			 return true;
		 }else{
			 return false;
		 }        
	}

	
	public function getToday(){		
		$stmt = $this->con->prepare("SELECT DATE_FORMAT(NOW(),'%Y%m%d');");
			$stmt->execute(); 
			$stmt->bind_result($date);
            $stmt->fetch();
									
			return $date;
	}
	
	public function getYesterday(){		
		$stmt = $this->con->prepare("SELECT DATE_FORMAT(NOW()- interval 1 day,'%Y%m%d');");
			$stmt->execute(); 
			$stmt->bind_result($date);
            $stmt->fetch();
									
			return $date;
	}
	
	public function getLastDigitIdFromTbReportOperational(){
		$stmt = $this->con->prepare("Select SUBSTR(MAX(id_report_all),13,3) FROM tb_report_operational");
		$stmt->execute(); 
		$stmt->bind_result($lastDigit);
		$stmt->fetch();
		return $lastDigit;
	}
	
	public function getLastReportOperational(){
		$id = null;
		$today = $this -> getToday();
		$lastDigit = $this -> getLastDigitIdFromTbReportOperational();
		$stmt1 = $this->con->prepare("select MAX(tgl_trx_operational) FROM tb_report_operational");
		$stmt1->execute(); 
		$stmt1->bind_result($maxtgl);
        $stmt1->fetch();
		$dateRes = str_replace("-","",$maxtgl);
		if($today > $dateRes){
			$id = 0;			
		}
		if($today == $dateRes){			
			$id = $lastDigit;
		}											
		return $id;
	}
	
	//-----------------------------------------------------------------------------------------------------------------------//	
	
	//-----------------------------------------TRX VIRTUAL-------------------------------------------------------------------//
		
	public function getLastTrxVirtualAcc(){
		$tgl_trx_virtual_acc = $this -> getToday();	
		$stmt = $this->con->prepare("Select SUBSTR(MAX(id_trx_virtual_acc),13,3) FROM tb_trx_virtual_acc");
		$stmt->execute(); 
		$stmt->bind_result($lastDigit);
        $stmt->fetch();
		$number = $lastDigit+ 1;
		$number = sprintf('%03d',$number);			
		$id_trx_virtual_acc = "OP{$tgl_trx_virtual_acc}{$number}";			
		return $id_trx_virtual_acc;
	}
		
		
	public function getTotalPendapatan(){
		$stmt = $this->con->prepare("SELECT id_method_bayar, sum(tagihan_akhir-komisi) as total_pendapatan FROM tb_trx_penjualan WHERE id_method_bayar != 1 and jenis_tagihan = 'Lunas' and DATE_FORMAT(tgl_trx_bayar,'%Y-%m-%d') = DATE_FORMAT(NOW(),'%Y-%m-%d') group by id_method_bayar");
		$stmt->execute(); 
		$stmt->bind_result($id_method_bayar,$total_earn);
		$arrayTotal_Pendapatan = array();
        while($stmt->fetch()){
			$total_pendapatan = array();
			$total_pendapatan ['id_method_bayar'] = $id_method_bayar;
			$total_pendapatan ['total_pendapatan'] = $total_earn;
			array_push($arrayTotal_Pendapatan, $total_pendapatan);
		}		
		return $arrayTotal_Pendapatan;
	}
	
	public function cekHari(){
		$stmt = $this->con->prepare("SELECT DAYNAME(now() - interval 1 day)");
		$stmt->execute(); 
		$stmt->bind_result($hari);
		$stmt->fetch();		
		return $hari;
	}
	
	public function getLastEntryVACC(){
		$stmt = $this->con->prepare("select MAX(tgl_trx_virtual_acc), id_method_bayar FROM tb_trx_virtual_acc GROUP BY id_method_bayar");
		$stmt->execute(); 
		$stmt->bind_result($tgl_trx_virtual_acc,$id_method_bayar);
		$arrayResult = array();
		while($stmt->fetch()){
			$result = array();
			$result ['id_method_bayar'] = $id_method_bayar;
			$result ['tgl_trx_virtual_acc'] = $tgl_trx_virtual_acc;
			array_push($arrayResult, $result);
		}
		
		return $arrayResult;
	}
	
	public function getFinalResultFromVACC(){
		
		
	}
	
	public function getSaldoAkhirMethodBayar(){
		$arrayResult = $this -> getLastEntryVACC();
		$arraySaldo_Akhir_Acc = array();
		for($i = 0;$i < count($arrayResult);$i++){				
			$stmt = $this->con->prepare("SELECT id_method_bayar, saldo_akhir_acc FROM tb_trx_virtual_acc WHERE tgl_trx_virtual_acc = ? and id_method_bayar = ?");
			$stmt->bind_param('ss',$arrayResult[$i]['tgl_trx_virtual_acc'],$arrayResult[$i]['id_method_bayar']);
			$stmt->execute();
			$stmt->bind_result($id_method_bayar,$saldo_akhir_acc);
			while($stmt->fetch()){
				$result = array();
				$result ['id_method_bayar'] = $id_method_bayar;
				$result ['saldo_akhir_acc'] = $saldo_akhir_acc;
				array_push($arraySaldo_Akhir_Acc, $result);
			}
		}		
		return $arraySaldo_Akhir_Acc;
	}
	
	public function cekDataAcc(){
		$stmt = $this->con->prepare("Select id_method_bayar,tgl_trx_virtual_acc from tb_trx_virtual_acc where tgl_trx_virtual_acc = (DATE_FORMAT(now(),'%Y-%m-%d'))");
		$stmt->execute(); 
		$stmt->bind_result($id_method_bayar,$tgl_trx_virtual);
		$arrayData_Acc = array();
        while($stmt->fetch()){
			$data_acc = array();
			$data_acc ['id_method_bayar'] = $id_method_bayar;
			$data_acc ['tgl_trx_virtual_acc'] = $tgl_trx_virtual;
			array_push($arrayData_Acc, $data_acc);
		}
		if(count($arrayData_Acc) > 0){
			return $arrayData_Acc;
		}else{
			return null;
		}		
	}
	public function getDateToday(){		
		$stmt = $this->con->prepare("SELECT DATE_FORMAT(NOW(),'%Y-%m-%d');");
			$stmt->execute(); 
			$stmt->bind_result($date);
            $stmt->fetch();
									
			return $date;
	}
	
	
	public function InsertLaporanVirtualACC(){		
		$listTotalPendapatan = $this -> getTotalPendapatan();
		$listSaldoAhir = $this -> getSaldoAkhirMethodBayar();
		$cekData = $this -> cekDataAcc();
		if(!empty($listTotalPendapatan)){
			if(!empty($listSaldoAhir)){
				if(count($listTotalPendapatan) == count($listSaldoAhir)){
					if(empty($cekData)){
						$saldo_baru_method_bayar = array();
						for($i = 0;$i < count($listTotalPendapatan);$i++){
							$saldoArray = array();	
							$saldoArray ['id_method_bayar'] = $listTotalPendapatan[$i]['id_method_bayar'];
							$saldoArray ['total_pendapatan'] = $listTotalPendapatan[$i]['total_pendapatan'];
							$saldoArray ['saldo_akhir_acc'] = 0;//$listTotalPendapatan[$i]['total_pendapatan'] + $listSaldoAhir[$i]['saldo_akhir_acc'];
							array_push($saldo_baru_method_bayar, $saldoArray);	
						}						
							for($i = 0;$i < count($saldo_baru_method_bayar);$i++){
								$listID = $this -> getLastTrxVirtualAcc();				 
								$debit = 0;	
								$tgl = $this -> getToday();
								$stmt = $this->con->prepare("INSERT INTO tb_trx_virtual_acc(id_trx_virtual_acc, tgl_trx_virtual_acc, id_method_bayar, kredit, debit, saldo_akhir_acc) VALUES (?,?,?,?,?,?);");
								$stmt->bind_param("ssssss",$listID,$tgl,$saldo_baru_method_bayar[$i]['id_method_bayar'],$saldo_baru_method_bayar[$i]['total_pendapatan'],$debit,$saldo_baru_method_bayar[$i]['saldo_akhir_acc']);
								$stmt->execute();
								/*if($stmt->execute()){
									return true;
								}else{
									return false;
								}*/
							}
						}else{
							return true;
						}	
					//echo 'list Total Pendapatan dan list Saldo Ahir Sama';			
				}else if(count($listTotalPendapatan) > count($listSaldoAhir)){
					if(empty($cekData)){
					$saldo_baru_method_bayarNew = array();	
					$saldo_baru_method_bayarExist = array();	
					for($i = 0;$i < count($listTotalPendapatan);$i++){
						$duplicate = false;
							for($j = 0;$j < count($listSaldoAhir);$j++){
								if($listTotalPendapatan[$i]['id_method_bayar'] == $listSaldoAhir[$j]['id_method_bayar']){
									$duplicate = true;
									$saldoArray = array();	
									$saldoArray ['id_method_bayar'] = $listTotalPendapatan[$i]['id_method_bayar'];
									$saldoArray ['total_pendapatan'] = $listTotalPendapatan[$i]['total_pendapatan'];
									$saldoArray ['saldo_akhir_acc'] = 0;//$listTotalPendapatan[$i]['total_pendapatan'] + $listSaldoAhir[$j]['saldo_akhir_acc'];
									array_push($saldo_baru_method_bayarExist, $saldoArray);	
								}
							}
							if(!$duplicate){																
									$saldoArray = array();	
									$saldoArray ['id_method_bayar'] = $listTotalPendapatan[$i]['id_method_bayar'];
									$saldoArray ['total_pendapatan'] = $listTotalPendapatan[$i]['total_pendapatan'];
									$saldoArray ['saldo_akhir_acc'] = 0;//$listTotalPendapatan[$i]['total_pendapatan'];
									array_push($saldo_baru_method_bayarNew, $saldoArray);																						
							}						
					}
					for($i = 0;$i < count($saldo_baru_method_bayarNew);$i++){						
						$listID = $this -> getLastTrxVirtualAcc();				 
						$debit = 0;	
						$tgl = $this -> getToday();
						$stmt = $this->con->prepare("INSERT INTO tb_trx_virtual_acc(id_trx_virtual_acc, tgl_trx_virtual_acc, id_method_bayar, kredit, debit, saldo_akhir_acc) VALUES (?,?,?,?,?,?);");
						$stmt->bind_param("ssssss",$listID,$tgl,$saldo_baru_method_bayarNew[$i]['id_method_bayar'],$saldo_baru_method_bayarNew[$i]['total_pendapatan'],$debit,$saldo_baru_method_bayarNew[$i]['saldo_akhir_acc']);
						$stmt->execute();
						/*if($stmt->execute()){
							return true;
						}else{
							return false;
						}*/			
					}
					for($i = 0;$i < count($saldo_baru_method_bayarExist);$i++){						
						$listID = $this -> getLastTrxVirtualAcc();				 
						$debit = 0;	
						$tgl = $this -> getToday();
						$stmt = $this->con->prepare("INSERT INTO tb_trx_virtual_acc(id_trx_virtual_acc, tgl_trx_virtual_acc, id_method_bayar, kredit, debit, saldo_akhir_acc) VALUES (?,?,?,?,?,?);");
						$stmt->bind_param("ssssss",$listID,$tgl,$saldo_baru_method_bayarExist[$i]['id_method_bayar'],$saldo_baru_method_bayarExist[$i]['total_pendapatan'],$debit,$saldo_baru_method_bayarExist[$i]['saldo_akhir_acc']);
						$stmt->execute();
						/*if($stmt->execute()){
							return true;
						}else{
							return false;
						}*/			
					}
					//echo 'list Total Pendapatan lebih besar';
					}else{
						return false;
					}					
				}else if(count($listTotalPendapatan) < count($listSaldoAhir)){
					if(empty($cekData)){
						$saldo_baru_method_bayarNew = array();	
						$saldo_baru_method_bayarExist = array();	
						for($i = 0;$i < count($listTotalPendapatan);$i++){
							$duplicate = false;
							for($j = 0;$j < count($listSaldoAhir);$j++){
								if($listTotalPendapatan[$i]['id_method_bayar'] == $listSaldoAhir[$j]['id_method_bayar']){
									$duplicate = true;
									$saldoArray = array();	
									$saldoArray ['id_method_bayar'] = $listTotalPendapatan[$i]['id_method_bayar'];
									$saldoArray ['total_pendapatan'] = $listTotalPendapatan[$i]['total_pendapatan'];
									$saldoArray ['saldo_akhir_acc'] = 0;//$listTotalPendapatan[$i]['total_pendapatan'] + $listSaldoAhir[$j]['saldo_akhir_acc'];
									array_push($saldo_baru_method_bayarExist, $saldoArray);								
								}									
							}
							if(!$duplicate){
								$saldoArray = array();	
								$saldoArray ['id_method_bayar'] = $listTotalPendapatan[$i]['id_method_bayar'];
								$saldoArray ['total_pendapatan'] = $listTotalPendapatan[$i]['total_pendapatan'];
								$saldoArray ['saldo_akhir_acc'] = 0;//$listTotalPendapatan[$i]['total_pendapatan'];
								array_push($saldo_baru_method_bayarNew, $saldoArray);	
							}
						}
						for($i = 0;$i < count($saldo_baru_method_bayarNew);$i++){						
							$listID = $this -> getLastTrxVirtualAcc();				 
							$debit = 0;	
							$tgl = $this -> getToday();
							$stmt = $this->con->prepare("INSERT INTO tb_trx_virtual_acc(id_trx_virtual_acc, tgl_trx_virtual_acc, id_method_bayar, kredit, debit, saldo_akhir_acc) VALUES (?,?,?,?,?,?);");
							$stmt->bind_param("ssssss",$listID,$tgl,$saldo_baru_method_bayarNew[$i]['id_method_bayar'],$saldo_baru_method_bayarNew[$i]['total_pendapatan'],$debit,$saldo_baru_method_bayarNew[$i]['saldo_akhir_acc']);
							$stmt->execute();
							/*if($stmt->execute()){
								return true;
							}else{
								return false;
							}*/			
						}
						for($i = 0;$i < count($saldo_baru_method_bayarExist);$i++){						
							$listID = $this -> getLastTrxVirtualAcc();				 
							$debit = 0;	
							$tgl = $this -> getToday();
							$stmt = $this->con->prepare("INSERT INTO tb_trx_virtual_acc(id_trx_virtual_acc, tgl_trx_virtual_acc, id_method_bayar, kredit, debit, saldo_akhir_acc) VALUES (?,?,?,?,?,?);");
							$stmt->bind_param("ssssss",$listID,$tgl,$saldo_baru_method_bayarExist[$i]['id_method_bayar'],$saldo_baru_method_bayarExist[$i]['total_pendapatan'],$debit,$saldo_baru_method_bayarExist[$i]['saldo_akhir_acc']);
							$stmt->execute();
							/*if($stmt->execute()){
								return true;
							}else{
								return false;
							}*/							
						}
						//echo 'listSaldoAhir lebih besar'.count($saldo_baru_method_bayarNew);
					}else{
						//echo 'sudah ada data';
						return false;
					}					
				}
			}else{			
				$date = $this -> getDateToday();
				$saldo_baru_method_bayar = array();
					for($i = 0;$i < count($listTotalPendapatan);$i++){	
						$saldoArray = array();	
						$saldoArray ['id_method_bayar'] = $listTotalPendapatan[$i]['id_method_bayar'];
						$saldoArray ['total_pendapatan'] = $listTotalPendapatan[$i]['total_pendapatan'];
						$saldoArray ['saldo_akhir_acc'] = 0;//$listTotalPendapatan[$i]['total_pendapatan'];
						array_push($saldo_baru_method_bayar, $saldoArray);				 
					}					
				if(empty($cekData)){
						for($i = 0;$i < count($saldo_baru_method_bayar);$i++){						
						$listID = $this -> getLastTrxVirtualAcc();				 
						$debit = 0;	
						$tgl = $this -> getToday();
						$stmt = $this->con->prepare("INSERT INTO tb_trx_virtual_acc(id_trx_virtual_acc, tgl_trx_virtual_acc, id_method_bayar, kredit, debit, saldo_akhir_acc) VALUES (?,?,?,?,?,?);");
						$stmt->bind_param("ssssss",$listID,$tgl,$saldo_baru_method_bayar[$i]['id_method_bayar'],$saldo_baru_method_bayar[$i]['total_pendapatan'],$debit,$saldo_baru_method_bayar[$i]['saldo_akhir_acc']);
						$stmt->execute();
						/*if($stmt->execute()){
							return true;
						}else{
							return false;
						}*/			
					}				
				}else{
					//echo 'Sudah ada Data';
					return false;
				}					
			}
		}else{
			//echo 'Belum ada transaksi online payment hari ini';
			return false;
		}
		
			
	}
	
	//-----------------------------------------------------------------------------------------------------------------------//	
	
	public function InsertLaporanOperational(){		
		if($this->getPenjualanHarianCash() != null){
			if($this -> updateSaldoAkhir()){
				if($this -> getToday() > $this -> getYesterday()){
					if($this -> getSaldoAkhir() != null){
						//-------------INPUT KAS HARIAN----------------------------------
						$tgl_trx_operational = $this -> getToday();	
						$number1 = $this -> getLastReportOperational() + 1;
						$number1 = sprintf('%03d',$number1);			
						$id_report_all = "OP{$tgl_trx_operational}{$number1}";
						$keterangan = 'Kas Harian';
						$kas_harian = $this -> getSaldoAkhir();
						$debit_op = 0;			
						$KasHarian = $this->InserTbReportOperational($id_report_all,$tgl_trx_operational,$keterangan,$kas_harian,$debit_op);
					
						//-------------INPUT KAS OPERATIONAL-----------------------------
						$tgl_trx_operational2 = $this -> getToday();						
						$number2 = $this -> getLastReportOperational() + 1;
						$number2 = sprintf('%03d',$number2);			
						$id_report_all2 = "OP{$tgl_trx_operational2}{$number2}";
						$keterangan2 = 'Pengeluaran Operational';
						$pOperational = 0;
						$debit_op2 = $this -> getTotalPengeluaranOP();		
					
						$PengeluaranOperational = $this->InserTbReportOperational($id_report_all2,$tgl_trx_operational2,$keterangan2,$pOperational,$debit_op2);
					
						//-----------------INPUT KE TABLE VIRTUAL ACC--------------------------
						$tgl_trx_operational3 = $this -> getToday();						
						$number3 = $this -> getLastReportOperational() + 1;
						$number3 = sprintf('%03d',$number3);			
						$id_report_all3 = "OP{$tgl_trx_operational3}{$number3}";
						$keterangan3 = 'Pendapatan Virtual ACC';
						$virtualAcc = 0;
						$debit_op3 =  $this -> getPendapatanVirtualACC();					
						$pendapatankeVirtualACC = $this->InserTbReportOperational($id_report_all3,$tgl_trx_operational3,$keterangan3,$virtualAcc,$debit_op3);	
											
						$arrayMessage = array();
						if(!$KasHarian){
							$message = array();
							if(!$this -> cekInputTbReportOperational($tgl_trx_operational,$keterangan)){
								$message['error'] = true; 
								$message['message'] = 'Gagal Saat Input Data Kas Harian';
							}else{								
								$message['error'] = true; 
								$message['message'] = 'Anda Sudah Melakukan Input Data '.$keterangan;								
							}														
							array_push($arrayMessage, $message);
						}else{
							$message = array(); 							
							$message['error'] = false; 
							$message['message'] = 'Input Data Kas Harian Berhasil';	
							array_push($arrayMessage, $message);
						}
						if(!$PengeluaranOperational){
							$message = array(); 
							if(!$this -> cekInputTbReportOperational($tgl_trx_operational2,$keterangan2)){
								$message['error'] = true; 
								$message['message'] = 'Gagal Saat Input Data Pengeluaran Operational';
							}else{								
								$message['error'] = true; 
								$message['message'] = 'Anda Sudah Melakukan Input Data '.$keterangan2;								
							}														
							array_push($arrayMessage, $message);
						}else{
							$message = array();								
							$message['error'] = false; 
							$message['message'] = 'Input Data Pengeluaran Operational Berhasil';							
							array_push($arrayMessage, $message);
						}	
						if(!$pendapatankeVirtualACC){
							$message = array();
							if(!$this -> cekInputTbReportOperational($tgl_trx_operational3,$keterangan3)){
								$message['error'] = true; 
								$message['message'] = 'Gagal Saat Input Data Pendapatan Virtual ACC';	
							}else{								
								$message['error'] = true; 
								$message['message'] = 'Anda Sudah Melakukan Input Data '.$keterangan3;								
							}																					
							array_push($arrayMessage, $message);
						}else{
							$message = array(); 							
							$message['error'] = false; 
							$message['message'] = 'Input Data Pendapatan Virtual ACC Berhasil';								
							array_push($arrayMessage, $message);
						}		
						if($KasHarian && $PengeluaranOperational && $pendapatankeVirtualACC){
							$message = array(); 
							$message['error'] = false; 
							$message['message'] = 'Success';
							return $message;
						}else{
							return $arrayMessage;
						}																													
					}else{
						$message = array(); 
						$message['error'] = true; 
						$message['message'] = 'Gagal Saat Mengambil Saldo Akhir';
						return $message;
					}			
				}else{
					$message = array(); 
					$message['error'] = true; 
					$message['message'] = 'Setting Tanggal Gagal';
					return $message;
				}
			}else{
				$message = array(); 
				$message['error'] = true; 
				$message['message'] = 'Update Saldo Akhir Gagal';
				return $message;
			}
		}else{
			$message = array(); 
            $message['error'] = true; 
            $message['message'] = 'Belum Ada transaksi HAri Ini';
			return $message;
		}
	}
	
		//-------------------------------------------------------Laporan Buku besar-----------------------------------------------------------------------
	public function laporanBukuBesar($tglawal,$tglakhir){
		    $arrayResult = array();
		    $datasum = $this -> sumBukuBesar($tglawal,$tglakhir);
		    $kas_awal = $this->kasawal($tglawal);
		    if($tglawal != null && $tglakhir != null || $tglawal != "" && $tglakhir != ""){
		        	$stmt = $this->con->prepare("SELECT `id_report_all`, `tgl_trx_operational`, `keterangan`, `kredit_op`, `debit_op` FROM `tb_report_operational` WHERE `tgl_trx_operational` BETWEEN '$tglawal' AND '$tglakhir' order by tgl_trx_operational , id_report_all");
                    $stmt->execute(); 
		            $stmt->bind_result($id_report_all,$tgl_trx_operational,$keterangan,$kredit_op,$debit_op);
		            while($stmt->fetch()){
			        $result = array();
			        $result ['id_report_all'] = $id_report_all;
			        $result ['tgl_trx_operational'] = $tgl_trx_operational;
			        $result ['keterangan'] = $keterangan;
			        $result ['kredit_op'] = $kredit_op;
			        $result ['debit_op'] = $debit_op;
			        array_push($arrayResult, $result);
		            }
		    }else{
		        $stmt = $this->con->prepare("SELECT `id_report_all`, `tgl_trx_operational`, `keterangan`, `kredit_op`, `debit_op` FROM `tb_report_operational` WHERE `tgl_trx_operational` = CURRENT_DATE");
		        $stmt->execute(); 
		        $stmt->bind_result($id_report_all,$tgl_trx_operational,$keterangan,$kredit_op,$debit_op);
		        while($stmt->fetch()){
			    $result = array();
			    $result ['id_report_all'] = $id_report_all;
			    $result ['tgl_trx_operational'] = $tgl_trx_operational;
			    $result ['keterangan'] = $keterangan;
			    $result ['kredit_op'] = $kredit_op;
			    $result ['debit_op'] = $debit_op;
			    array_push($arrayResult, $result);
		        }
		    }
		 $arrayDataResult = array();
		 $arr = array();
		 $arr['arrayListReportBukuBesar'] = $arrayResult;
		 $arr['arrayKreditDebit'] = $datasum;
		 $arr['kas_awal'] = $kas_awal;
		  array_push($arrayDataResult, $arr);
		return $arrayDataResult;
	}
	
	public function kasawal($tglawal){
	    $result = 0;
		    if($tglawal != null|| $tglawal != ""){
		        	$stmt = $this->con->prepare("SELECT (SUM(`kredit_op`)-SUM(`debit_op`)) FROM `tb_report_operational` WHERE `tgl_trx_operational` <= '$tglawal'");
                    $stmt->execute(); 
		            $stmt->bind_result($kas_awal);
		            $stmt->fetch();
		            $result = $kas_awal;
		    }else{
		        $stmt = $this->con->prepare("SELECT (SUM(`kredit_op`)-SUM(`debit_op`)) FROM `tb_report_operational`");
		        $stmt->execute(); 
		         $stmt->bind_result($kas_awal);
		         $stmt->fetch();
		         $result = $kas_awal;
		}
			return $result;
	}
	
	public function sumBukuBesar($tglawal,$tglakhir){
	    $arrayResult = array();
		    if($tglawal != null && $tglakhir != null || $tglawal != "" && $tglakhir != ""){
		        	$stmt = $this->con->prepare("SELECT SUM(`kredit_op`), SUM(`debit_op`) FROM `tb_report_operational` WHERE `tgl_trx_operational` BETWEEN '$tglawal' AND '$tglakhir'");
                    $stmt->execute(); 
		            $stmt->bind_result($kredit_op,$debit_op);
		            while($stmt->fetch()){
			        $result = array();
			        $result ['kredit_op'] = $kredit_op;
			        $result ['debit_op'] = $debit_op;
			        array_push($arrayResult, $result);
		            }
		    }else{
		        $stmt = $this->con->prepare("SELECT SUM(`kredit_op`), SUM(`debit_op`) FROM `tb_report_operational` WHERE `tgl_trx_operational`");
		        $stmt->execute(); 
		         $stmt->bind_result($kredit_op,$debit_op);
		        while($stmt->fetch()){
			    $result = array();
			    $result ['kredit_op'] = $kredit_op;
			    $result ['debit_op'] = $debit_op;
			    array_push($arrayResult, $result);
		        }
		}
			return $arrayResult;
	}
		        
	//-----------------------------------------------------------------------------------------------------------------------------------------------
	
	//-------------------------------------------------------Laporan Penjualan-----------------------------------------------------------------------
	
	public function laporan_penjualan($tglawal,$tglakhir,$method_bayar){
	     $arrayResult = array();
	     $sum_komisi = $this->komisi_laporan_penjualan($tglawal,$tglakhir,$method_bayar);
	     $saldo = $this->saldo_laporan_penjualan($tglawal,$tglakhir,$method_bayar);
	    if($tglawal == "" && $tglakhir == "" && $method_bayar == "" || $tglawal == null && $tglakhir == null && $method_bayar == null){
	        $stmt = $this->con->prepare("SELECT DATE_FORMAT(tgl_trx_bayar,'%Y-%m-%d'), nama_method, sum(tagihan_akhir-komisi), SUM(KOMISI) FROM tb_trx_penjualan JOIN tb_method_bayar ON tb_trx_penjualan.id_method_bayar = tb_method_bayar.id_method_bayar WHERE jenis_tagihan = 'Lunas' and DATE_FORMAT(tgl_trx_bayar,'%Y-%m-%d') = CURRENT_DATE group by DATE_FORMAT(tgl_trx_bayar,'%Y-%m-%d'),nama_method order by tgl_trx_bayar desc");
		        $stmt->execute(); 
		        $stmt->bind_result($tgl_trx_bayar,$nama_method,$debit,$komisi);
		        while($stmt->fetch()){
			    $result = array();
			    $result ['tgl_trx_bayar'] = $tgl_trx_bayar;
			    $result ['nama_method'] = $nama_method;
			    $result ['debit'] = $debit;
			    $result ['komisi'] = $komisi; 
			    array_push($arrayResult, $result);
	            }
	    }else if($tglawal != "" && $tglakhir != "" && $method_bayar == "" || $tglawal != null && $tglakhir != null && $method_bayar == null){
	        $stmt = $this->con->prepare("SELECT DATE_FORMAT(tgl_trx_bayar,'%Y-%m-%d'), nama_method, sum(tagihan_akhir-komisi), SUM(KOMISI)  FROM tb_trx_penjualan JOIN tb_method_bayar ON tb_trx_penjualan.id_method_bayar = tb_method_bayar.id_method_bayar WHERE jenis_tagihan = 'Lunas' and DATE_FORMAT(tgl_trx_bayar,'%Y-%m-%d') >= DATE_FORMAT('$tglawal','%Y-%m-%d') AND DATE_FORMAT(tgl_trx_bayar,'%Y-%m-%d') <= DATE_FORMAT('$tglakhir','%Y-%m-%d') group by DATE_FORMAT(tgl_trx_bayar,'%Y-%m-%d') ,nama_method order by tgl_trx_bayar DESC");
		        $stmt->execute(); 
		        $stmt->bind_result($tgl_trx_bayar,$nama_method,$debit,$komisi);
		        while($stmt->fetch()){
			    $result = array();
			    $result ['tgl_trx_bayar'] = $tgl_trx_bayar;
			    $result ['nama_method'] = $nama_method;
			    $result ['debit'] = $debit;
			    $result ['komisi'] = $komisi;
			    array_push($arrayResult, $result);
	            }
	    }else if($tglawal == "" && $tglakhir == "" && $method_bayar != "" || $tglawal == null && $tglakhir == null && $method_bayar != null){
	        $stmt = $this->con->prepare("SELECT DATE_FORMAT(tgl_trx_bayar,'%Y-%m-%d'), nama_method, sum(tagihan_akhir-komisi), SUM(KOMISI) FROM tb_trx_penjualan JOIN tb_method_bayar ON tb_trx_penjualan.id_method_bayar = tb_method_bayar.id_method_bayar WHERE jenis_tagihan = 'Lunas' and tb_method_bayar.`id_method_bayar` = $method_bayar group by DATE_FORMAT(tgl_trx_bayar,'%Y-%m-%d') ,nama_method order by tgl_trx_bayar DESC");
		        $stmt->execute(); 
		        $stmt->bind_result($tgl_trx_bayar,$nama_method,$debit,$komisi);
		        while($stmt->fetch()){
			    $result = array();
			    $result ['tgl_trx_bayar'] = $tgl_trx_bayar;
			    $result ['nama_method'] = $nama_method;
			    $result ['debit'] = $debit;
			    $result ['komisi'] = $komisi; 
			    array_push($arrayResult, $result);
	        }
	    }else if($tglawal != "" && $tglakhir != "" && $method_bayar != "" || $tglawal != null && $tglakhir != null && $method_bayar != null){
	        $stmt = $this->con->prepare("SELECT DATE_FORMAT(tgl_trx_bayar,'%Y-%m-%d'), nama_method, sum(tagihan_akhir-komisi), SUM(KOMISI)  FROM tb_trx_penjualan JOIN tb_method_bayar ON tb_trx_penjualan.id_method_bayar = tb_method_bayar.id_method_bayar WHERE jenis_tagihan = 'Lunas' and DATE_FORMAT(tgl_trx_bayar,'%Y-%m-%d') >= DATE_FORMAT('$tglawal','%Y-%m-%d') AND DATE_FORMAT(tgl_trx_bayar,'%Y-%m-%d') <= DATE_FORMAT('$tglakhir','%Y-%m-%d') AND tb_method_bayar.`id_method_bayar` = $method_bayar group by DATE_FORMAT(tgl_trx_bayar,'%Y-%m-%d') ,nama_method order by tgl_trx_bayar DESC");
		        $stmt->execute(); 
		        $stmt->bind_result($tgl_trx_bayar,$nama_method,$debit,$komisi);
		        while($stmt->fetch()){
			    $result = array();
			    $result ['tgl_trx_bayar'] = $tgl_trx_bayar;
			    $result ['nama_method'] = $nama_method;
			    $result ['debit'] = $debit;
			    $result ['komisi'] = $komisi;
			    array_push($arrayResult, $result);
	        }
	    }
	    $arrayData = array();
	    $arr = array();
	    $arr['list_laporan_penjualan'] = $arrayResult;
	    $arr['sum_komisi'] = $sum_komisi;
	    $arr['saldo'] = $saldo;
	    array_push($arrayData, $arr);
	  return $arrayData;
	}
	
	public function saldo_laporan_penjualan($tglawal,$tglakhir,$method_bayar){
	    $saldo_laporan_penjualan = 0;
	    if($tglawal != "" && $tglakhir != "" && $method_bayar == "" || $tglawal != null && $tglakhir != null && $method_bayar == null){
	        $stmt = $this->con->prepare("SELECT sum(tagihan_akhir-komisi) FROM tb_trx_penjualan JOIN tb_method_bayar ON tb_trx_penjualan.id_method_bayar = tb_method_bayar.id_method_bayar WHERE jenis_tagihan = 'Lunas' and DATE_FORMAT(tgl_trx_bayar,'%Y-%m-%d') >= DATE_FORMAT($tglawal,'%Y-%m-%d') AND DATE_FORMAT(tgl_trx_bayar,'%Y-%m-%d') <= DATE_FORMAT($tglakhir,'%Y-%m-%d') order by tgl_trx_bayar");
		        $stmt->execute(); 
		        $stmt->bind_result($saldo);
		        $stmt->fetch();
		        $saldo_laporan_penjualan = $saldo;
	    }else if($tglawal == "" && $tglakhir == "" && $method_bayar != "" || $tglawal == null && $tglakhir == null && $method_bayar != null){
	        $stmt = $this->con->prepare("SELECT sum(tagihan_akhir-komisi) FROM tb_trx_penjualan JOIN tb_method_bayar ON tb_trx_penjualan.id_method_bayar = tb_method_bayar.id_method_bayar WHERE jenis_tagihan = 'Lunas' AND tb_trx_penjualan.id_method_bayar = $method_bayar");
		        $stmt->execute(); 
		        $stmt->bind_result($saldo);
		        $stmt->fetch();
		        $saldo_laporan_penjualan = $saldo;
	    }else if($tglawal != "" && $tglakhir != "" && $method_bayar != "" || $tglawal != null && $tglakhir != null && $method_bayar != null){
	        $stmt = $this->con->prepare("SELECT sum(tagihan_akhir-komisi) FROM tb_trx_penjualan JOIN tb_method_bayar ON tb_trx_penjualan.id_method_bayar = tb_method_bayar.id_method_bayar WHERE jenis_tagihan = 'Lunas' and DATE_FORMAT(tgl_trx_bayar,'%Y-%m-%d') >= DATE_FORMAT($tglawal,'%Y-%m-%d') AND DATE_FORMAT(tgl_trx_bayar,'%Y-%m-%d') <= DATE_FORMAT($tglakhir,'%Y-%m-%d') AND tb_trx_penjualan.id_method_bayar = $method_bayar order by tgl_trx_bayar");
		        $stmt->execute(); 
		        $stmt->bind_result($saldo);
		        $stmt->fetch();
		        $saldo_laporan_penjualan = $saldo;
	    }
	    return $saldo_laporan_penjualan;
	}
	
	public function komisi_laporan_penjualan($tglawal,$tglakhir,$method_bayar){
	    $komisi_laporan_penjualan = 0;
	    if($tglawal != "" && $tglakhir != "" && $method_bayar == "" || $tglawal != null && $tglakhir != null && $method_bayar == null){
	        $stmt = $this->con->prepare("SELECT sum(komisi) FROM tb_trx_penjualan JOIN tb_method_bayar ON tb_trx_penjualan.id_method_bayar = tb_method_bayar.id_method_bayar WHERE jenis_tagihan = 'Lunas' and DATE_FORMAT(tgl_trx_bayar,'%Y-%m-%d') >= DATE_FORMAT($tglawal,'%Y-%m-%d') AND DATE_FORMAT(tgl_trx_bayar,'%Y-%m-%d') <= DATE_FORMAT($tglakhir,'%Y-%m-%d') order by tgl_trx_bayar");
		        $stmt->execute(); 
		        $stmt->bind_result($komisi);
		        $stmt->fetch();
		        $komisi_laporan_penjualan = $komisi;
	    }else if($tglawal == "" && $tglakhir == "" && $method_bayar != "" || $tglawal == null && $tglakhir == null && $method_bayar != null){
	        $stmt = $this->con->prepare("SELECT sum(komisi) FROM tb_trx_penjualan JOIN tb_method_bayar ON tb_trx_penjualan.id_method_bayar = tb_method_bayar.id_method_bayar WHERE jenis_tagihan = 'Lunas' AND tb_trx_penjualan.id_method_bayar = $method_bayar");
		        $stmt->execute(); 
		        $stmt->bind_result($komisi);
		        $stmt->fetch();
		        $komisi_laporan_penjualan = $komisi;
	    }else if($tglawal != "" && $tglakhir != "" && $method_bayar != "" || $tglawal != null && $tglakhir != null && $method_bayar != null){
	        $stmt = $this->con->prepare("SELECT sum(komisi) FROM tb_trx_penjualan JOIN tb_method_bayar ON tb_trx_penjualan.id_method_bayar = tb_method_bayar.id_method_bayar WHERE jenis_tagihan = 'Lunas' and DATE_FORMAT(tgl_trx_bayar,'%Y-%m-%d') >= DATE_FORMAT($tglawal,'%Y-%m-%d') AND DATE_FORMAT(tgl_trx_bayar,'%Y-%m-%d') <= DATE_FORMAT($tglakhir,'%Y-%m-%d') AND tb_trx_penjualan.id_method_bayar = $method_bayar order by tgl_trx_bayar");
		        $stmt->execute(); 
		        $stmt->bind_result($komisi);
		        $stmt->fetch();
		        $komisi_laporan_penjualan = $komisi;
	    }
	    return $komisi_laporan_penjualan;
	}
	
	//-----------------------------------------------------------------------------------------------------------------------------------------------
	
		//----------------------------------------------------------Virtual ACC----------------------------------------------------------------------
	
	public function withdrawl_acc($idmethodbayar,$tgl_trx_virtual_acc,$saldo){
	    $id_trx_virtual_acc = $this -> getLastTrxVirtualAcc();
	    $kredit = 0;
	    $saldo_akhir_acc = 0;
		$stmt = $this->con->prepare("INSERT INTO tb_trx_virtual_acc (id_trx_virtual_acc,tgl_trx_virtual_acc,id_method_bayar,kredit,debit,saldo_akhir_acc) VALUES (?,?,?,?,?,?)");
		$stmt->bind_param("ssssss",$id_trx_virtual_acc,$tgl_trx_virtual_acc,$idmethodbayar,$kredit,$saldo,$saldo_akhir_acc);
		if($stmt->execute()){
			return true;
		}else{
		return false;
		}
	}
//---	
	public function withdrawl_report_operational($idmethodbayar,$tgl_trx_virtual_acc,$saldo){
	    $keterangan = $this->getNamaMethodBayar($idmethodbayar);
	    $tgl_operational = $this -> getToday();
	    $number = $this -> getLastReportOperational() + 1;
		$number = sprintf('%03d',$number);			
	    $id_trx_virtual_acc = "OP{$tgl_operational}{$number}";
	    $debit_op = 0;
	    $saldo_akhir_acc = 0;
		$stmt = $this->con->prepare("INSERT INTO `tb_report_operational`(`id_report_all`, `tgl_trx_operational`, `keterangan`, `kredit_op`, `debit_op`) VALUES (?,?,?,?,?)");
		$stmt->bind_param("sssss",$id_trx_virtual_acc,$tgl_trx_virtual_acc,$keterangan,$saldo,$debit_op);
		if($stmt->execute()){
			return true;
		}else{
		return false;
		}
	}
	
	public function getNamaMethodBayar($idmethodbayar){
	        $nama_method = "";
	            $stmt = $this->con->prepare("SELECT nama_method FROM `tb_method_bayar` WHERE id_method_bayar = $idmethodbayar");
		        $stmt->execute(); 
		        $stmt->bind_result($method);
		        $stmt->fetch();
		        $nama_method = "Transfer Akun dari {$method}";//$method;
	    return $nama_method;
	}
	
	public function saldo_virtual_acc($method_bayar){
	    $arrayResult = array();
	    $stmt = $this->con->prepare("SELECT `id_method_bayar`,sum(`kredit`)-sum(`debit`) FROM `tb_trx_virtual_acc` WHERE id_method_bayar = $method_bayar");
		        $stmt->execute(); 
		        $stmt->bind_result($id_method_bayar,$saldo);
		        while($stmt->fetch()){
			    $result = array();
			    $result ['id_method_bayar'] = $id_method_bayar;
			    $result ['saldo'] = $saldo;
			    array_push($arrayResult, $result);
	            }
	    return $arrayResult;
	}
	
	public function list_virtual_acc(){
	    $arrayResult = array();
	     $stmt = $this->con->prepare("SELECT `nama_method`, sum(`kredit`)-sum(`debit`) FROM `tb_trx_virtual_acc` NATURAL JOIN tb_method_bayar group by `nama_method`");
		        $stmt->execute(); 
		        $stmt->bind_result($nama_method,$saldo);
		        while($stmt->fetch()){
			    $result = array();
			    $result ['nama_method'] = $nama_method;
			    $result ['saldo'] = $saldo;
			    array_push($arrayResult, $result);
	            }
	  return $arrayResult;
	}
	
		//-----------------------------------------------------------------------------------------------------------------------------------------------
	
}