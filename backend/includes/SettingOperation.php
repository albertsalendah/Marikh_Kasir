<?php

class SettingOperation{
	
	private $con; 

        function __construct(){
            require_once dirname(__FILE__) . '/DbConnect.php';
            $db = new DbConnect; 
            $this->con = $db->connect(); 
        }
		
		public function tambahSetting($modal_awal,$tax,$gofoodph,$gofoodec,$gofoodkomisi,$grabfoodph,$grabfoodec,$grabfoodkomisi){			
			$stmt = $this->con->prepare("INSERT INTO tb_setting (modal_awal,tax,gofoodph,gofoodec,gofoodkomisi,grabfoodph,grabfoodec,grabfoodkomisi) VALUES (?,?,?,?,?,?,?,?)");
			$stmt->bind_param("ssssssss",$modal_awal,$tax,$gofoodph,$gofoodec,$gofoodkomisi,$grabfoodph,$grabfoodec,$grabfoodkomisi);
			if($stmt->execute()){
				return true; 
			}else{
				return false;
			}
		}
		
		public function updateGofood($id,$gofoodph,$gofoodec,$gofoodkomisi){
				$stmt = $this->con->prepare("UPDATE tb_setting SET gofoodph = ?,gofoodec = ?,gofoodkomisi = ? WHERE id = ?");
				$stmt->bind_param("ssss",$gofoodph,$gofoodec,$gofoodkomisi,$id);
				if($stmt->execute())
					return true; 
				return false;			
        }
		
		public function updateGrabfood($id,$grabfoodph,$grabfoodec,$grabfoodkomisi){
				$stmt = $this->con->prepare("UPDATE tb_setting SET grabfoodph = ?,grabfoodec = ?,grabfoodkomisi = ? WHERE id = ?");
				$stmt->bind_param("ssss",$grabfoodph,$grabfoodec,$grabfoodkomisi,$id);
				if($stmt->execute())
					return true; 
				return false;			
        }
		
		public function updateModalAwal($id,$modal_awal){
				$stmt = $this->con->prepare("UPDATE tb_setting SET modal_awal = ? WHERE id = ?");
				$stmt->bind_param("ss",$modal_awal,$id);
				if($stmt->execute())
					return true; 
				return false;			
        }
		
		public function updateTax($id,$tax){
				$stmt = $this->con->prepare("UPDATE tb_setting SET tax = ? WHERE id = ?");
				$stmt->bind_param("ss",$tax,$id);
				if($stmt->execute())
					return true; 
				return false;			
        }
		
		public function ListSetting(){
            $stmt = $this->con->prepare("SELECT id,modal_awal,tax,gofoodph,gofoodec,gofoodkomisi,grabfoodph,grabfoodec,grabfoodkomisi FROM tb_setting;");
            $stmt->execute();
            $stmt->bind_result($id,$modal_awal,$tax,$gofoodph,$gofoodec,$gofoodkomisi,$grabfoodph,$grabfoodec,$grabfoodkomisi);
            $ArraySetting = array(); 
            while($stmt->fetch()){ 
                $Setting = array(); 
				$Setting['id'] = $id; 
                $Setting['modal_awal'] = $modal_awal; 
                $Setting['tax'] = $tax; 
                $Setting['gofoodph'] = $gofoodph; 
				$Setting['gofoodec'] = $gofoodec;
				$Setting['gofoodkomisi'] = $gofoodkomisi;
				$Setting['grabfoodph'] = $grabfoodph;
				$Setting['grabfoodec'] = $grabfoodec;
				$Setting['grabfoodkomisi'] = $grabfoodkomisi;
                array_push($ArraySetting, $Setting);
            }             
            return $ArraySetting; 
        }
		
		public function tambah_report_operational($id_report_all,$tgl_trx_operational,$keterangan,$kredit_op,$debit_op){
			$stmt = $this->con->prepare("INSERT INTO tb_report_operational (id_report_all,tgl_trx_operational,keterangan,kredit_op,debit_op) VALUES (?,?,?,?,?)");
			$stmt->bind_param("sssss",$id_report_all,$tgl_trx_operational,$keterangan,$kredit_op,$debit_op);
			if($stmt->execute()){
				return true; 
			}else{
				return false;
			}
		}
		
		public function ListReportOperational(){
            $stmt = $this->con->prepare("SELECT id_report_all,tgl_trx_operational,keterangan,kredit_op,debit_op FROM tb_report_operational;");
            $stmt->execute();
            $stmt->bind_result($id_report_all,$tgl_trx_operational,$keterangan,$kredit_op,$debit_op);
            $ArrayReportOperational = array(); 
            while($stmt->fetch()){ 
                $ReportOperational  = array(); 
				$ReportOperational ['id'] = $id; 
                $ReportOperational ['modal_awal'] = $modal_awal; 
                $ReportOperational ['tax'] = $tax; 
                $ReportOperational ['gofoodph'] = $gofoodph; 
				$ReportOperational ['gofoodec'] = $gofoodec;
				$ReportOperational ['gofoodkomisi'] = $gofoodkomisi;
				$ReportOperational ['grabfoodph'] = $grabfoodph;
				$ReportOperational ['grabfoodec'] = $grabfoodec;
				$ReportOperational ['grabfoodkomisi'] = $grabfoodkomisi;
                array_push($ArrayReportOperational, $ReportOperational);
            }             
            return $ArrayReportOperational; 
        }
		
}