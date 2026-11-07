<?php


use \Psr\Http\Message\ServerRequestInterface as Request;
use \Psr\Http\Message\ResponseInterface as Response;

require '../vendor/autoload.php';

require '../includes/UserOperation.php';


$app = new \Slim\App([
    'settings'=>[
        'displayErrorDetails'=>true
    ]
]);

$app->post('/getusername', function(Request $request, Response $response, array $args){ 
$request_data = $request->getParsedBody(); 
	
	$username = $request_data['username'];
	
	$db = new UserOperation; 
    if($user = $db->getUserByUsername($username)){
		$response->write(json_encode($user));
		return $response
		->withHeader('Content-type', 'application/json')
		->withStatus(200);
	}
});

$app->post('/userlogin', function(Request $request, Response $response){

    if(!haveEmptyParameters(array('username', 'password'), $request, $response)){
        $request_data = $request->getParsedBody(); 

        $username = $request_data['username'];
        $password = $request_data['password'];
        
        $db = new UserOperation; 

        $result = $db->userLogin($username, $password);

        if($result == USER_AUTHENTICATED){
            
            $user = $db->getUserByUsername($username);
            $response_data = array();

            $response_data['error']=false; 
            $response_data['message'] = 'Login Berhasil';
            $response_data['user']=$user; 

            $response->write(json_encode($response_data));

            return $response
                ->withHeader('Content-type', 'application/json')
                ->withStatus(200);    

        }else if($result == USER_NOT_FOUND){
            $response_data = array();

            $response_data['error']=true; 
            $response_data['message'] = 'User tidak terdaftar';

            $response->write(json_encode($response_data));

            return $response
                ->withHeader('Content-type', 'application/json')
                ->withStatus(200);    

        }else if($result == USER_PASSWORD_DO_NOT_MATCH){
            $response_data = array();

            $response_data['error']=true; 
            $response_data['message'] = 'Password Salah';

            $response->write(json_encode($response_data));

            return $response
                ->withHeader('Content-type', 'application/json')
                ->withStatus(200);  
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