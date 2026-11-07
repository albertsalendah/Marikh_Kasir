<?php

class ReportSaldoHarianOperation{
	
	private $con; 

        function __construct(){
            require_once dirname(__FILE__) . '/DbConnect.php';
            $db = new DbConnect; 
            $this->con = $db->connect(); 
        }
        
		
		public function InsertSaldoHarian($tgl_report_sh,$saldo_awal){
			if(!$this->jikaTglSudahAda($tgl_report_sh,$saldo_awal)){
                $stmt = $this->con->prepare("INSERT INTO tb_report_saldo_harian (tgl_report_sh,saldo_awal) VALUES (?,?)");
                $stmt->bind_param("ss", $tgl_report_sh,$saldo_awal);
                if($stmt->execute()){
                    return SALDO_BERHASIL_DITAMBAH; 
                }else{
                    return SALDO_GAGAL_DITAMBAH;
                }
			}
			return TGL_SUDAH_ADA;
        }
        
        public function InserTbReportOperational($saldo_awal){
            $tgl_trx_operational = $this -> getToday();	
						$number1 = $this -> getLastReportOperational() + 1;
						$number1 = sprintf('%03d',$number1);			
						$id_report_all = "OP{$tgl_trx_operational}{$number1}";
						$keterangan = 'Saldo Awal Harian';
						$kas_harian = 0;
						$debit_op = $saldo_awal;
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
		
		private function jikaTglSudahAda($tgl_report_sh,$saldo_awal){
            $stmt = $this->con->prepare("SELECT id_report_sh FROM tb_report_saldo_harian WHERE tgl_report_sh = ?");
            $stmt->bind_param("i", $tgl_report_sh);
            $stmt->execute(); 
            $stmt->store_result(); 
            return $stmt->num_rows > 0;  
        }
		
		public function ListSaldoHarian(){
            $stmt = $this->con->prepare("SELECT id_report_sh,tgl_report_sh,saldo_awal,saldo_akhir FROM tb_report_saldo_harian;");
            $stmt->execute();
            $stmt->bind_result($id_report_sh,$tgl_report_sh, $saldo_awal,$saldo_akhir);
            $ArraySaldoHarian = array(); 
            while($stmt->fetch()){ 
                $SaldoHarian = array(); 
				$SaldoHarian['id_report_sh'] = $id_report_sh; 
                $SaldoHarian['tgl_report_sh'] = $tgl_report_sh; 
                $SaldoHarian['saldo_awal'] = $saldo_awal; 
                $SaldoHarian['saldo_akhir'] = $saldo_akhir; 
                array_push($ArraySaldoHarian, $SaldoHarian);
            }             
            return $ArraySaldoHarian; 
        }
		
		public function updateSaldoHarian($id_report_sh,$tgl_report_sh, $saldo_awal,$saldo_akhir){
            $stmt = $this->con->prepare("UPDATE tb_report_saldo_harian SET tgl_report_sh = ?,saldo_awal = ?,saldo_akhir = ? WHERE id_report_sh = ?");
            $stmt->bind_param("ssss",$tgl_report_sh, $saldo_awal,$saldo_akhir,$id_report_sh);
            if($stmt->execute())
                return true; 
            return false; 
        }
		
		public function getReportSaldoHarianByID($id_report_sh){
            $stmt = $this->con->prepare("SELECT id_report_sh,tgl_report_sh,saldo_awal,saldo_akhir FROM tb_report_saldo_harian WHERE id_report_sh = ?");
            $stmt->bind_param("s", $id_report_sh);
            $stmt->execute(); 
            $stmt->bind_result($id_report_sh,$tgl_report_sh, $saldo_awal,$saldo_akhir);
            $stmt->fetch(); 
            $SaldoHarian = array(); 
            $SaldoHarian['id_report_sh']  = $id_report_sh; 
            $SaldoHarian['tgl_report_sh'] = $tgl_report_sh; 
			$SaldoHarian['saldo_awal']  = $saldo_awal;
			$SaldoHarian['saldo_akhir']   = $saldo_akhir;
           
            return $SaldoHarian; 
        }
		
		public function getReportSaldoHarianByTgl($tgl_report_sh){
            $stmt = $this->con->prepare("SELECT id_report_sh,tgl_report_sh,saldo_awal,saldo_akhir FROM tb_report_saldo_harian WHERE tgl_report_sh = ?");
            $stmt->bind_param("s", $tgl_report_sh);
            $stmt->execute(); 
            $stmt->bind_result($id_report_sh,$tgl_report_sh, $saldo_awal,$saldo_akhir);
            $stmt->fetch(); 
            $SaldoHarian = array(); 
            $SaldoHarian['id_report_sh']  = $id_report_sh; 
            $SaldoHarian['tgl_report_sh'] = $tgl_report_sh; 
			$SaldoHarian['saldo_awal']  = $saldo_awal;
			$SaldoHarian['saldo_akhir']   = $saldo_akhir;
           
            return $SaldoHarian; 
        }
        
        public function getToday(){		
		$stmt = $this->con->prepare("SELECT DATE_FORMAT(NOW(),'%Y%m%d');");
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
		
}