<?php

use App\Http\Controllers\UserController;
use App\Http\Controllers\PresenceController;
use App\Http\Controllers\ReportController;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Route;

/*
|--------------------------------------------------------------------------
| API Routes
|--------------------------------------------------------------------------
|
| Here is where you can register API routes for your application. These
| routes are loaded by the RouteServiceProvider within a group which
| is assigned the "api" middleware group. Enjoy building your API!
|
*/

Route::post('login', [UserController::class, 'login']);

Route::middleware(['auth:api'])->group(function () {

    //user
    Route::post('register', [UserController::class, 'register']);
    Route::get('user', [UserController::class, 'index']);
    Route::post('logout', [UserController::class, 'logout']);
    Route::post('edit-user', [UserController::class, 'edit']);
    Route::post('edit-image-user', [UserController::class, 'editImage']);
    Route::post('delete-user', [UserController::class, 'deleteUser']);
    Route::post('show-user', [UserController::class, 'showUser']);

    //presence
    Route::post('show-presence', [PresenceController::class, 'showPresence']);
    Route::post('add-presence', [PresenceController::class, 'addPresence']);
    Route::post('off-presence', [PresenceController::class, 'offPresence']);
    Route::post('back-presence', [PresenceController::class, 'backPresence']);
    Route::post('verify-presence', [PresenceController::class, 'verificationPresence']);
    Route::post('delete-presence', [PresenceController::class, 'deletePresence']);
    Route::get('location-presence', [PresenceController::class, 'getLocation']);
    Route::get('get-presence-today', [PresenceController::class, 'getPresenceTOday']);
    Route::post('create-pdf-presence', [PresenceController::class, 'createPDF']);

    //report
    Route::post('show-report', [ReportController::class, 'showReport']);
    Route::post('add-report', [ReportController::class, 'addReport']);
    Route::post('verify-report', [ReportController::class, 'verificationReport']);
    Route::post('delete-report', [ReportController::class, 'deleteReport']);
    Route::post('create-pdf-report', [ReportController::class, 'createPDF']);


});