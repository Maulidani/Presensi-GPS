<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\Models\Report;
use App\Models\User;
use Illuminate\Support\Facades\Auth;
use Illuminate\Support\Carbon;

class ReportController extends Controller
{
    public function showReport(Request $request)
    {
        $userId = $request->user()->id;
        $userPosition = $request->user()->position;

        if ($userPosition === 'sales') {
            $presence = Report::join('users', 'reports.user_id', '=', 'users.id')
                ->where('reports.user_id', $userId)
                ->orderBy('reports.created_at', 'DESC')
                ->get(['users.image as image_user', 'users.*', 'reports.*']);
        } else {
            $presence = Report::join('users', 'reports.user_id', '=', 'users.id')
                ->orderBy('reports.created_at', 'DESC')
                ->get(['users.image as image_user', 'users.*', 'reports.*']);
        }

        return response()->json([
            'message' => 'Success',
            'status' =>true,
            'data' => $presence,
        ]);
    }

    public function addreport(Request $request)
    {
        $exist = Report::where('user_id', '=', $request->user()->id)
            ->where('latitude', '=', $request->latitude)
            ->where('longitude', '=', $request->longitude)
            ->whereDate('created_at', today())
            ->exists();

        if ($exist) {
            return response()->json([
                'message' => 'Anda telah mengirim laporan di tempat ini & hari ini ',
                'status' => false
            ]);
        } else {

            $files = $request->image;
            $allowedfileExtension = ['jpeg', 'jpg', 'png', 'JPG', 'JPEG'];
            if ($request->hasfile('image')) {

                $filename = time() . '.' . $files->getClientOriginalName();
                $extension = $files->getClientOriginalExtension();

                $check = in_array($extension, $allowedfileExtension);

                if ($check) {

                    $files->move(public_path() . '/image/report/', $filename);

                    $result = Report::create([
                        'user_id' => $request->user()->id,
                        'name' => $request->name,
                        'latitude' => $request->latitude,
                        'longitude' => $request->longitude,
                        'note' => $request->note,
                        'image' => $filename,
                    ]);

                    if ($result) {
                        return response()->json([
                            'message' => 'Success',
                            'status' => true
                        ]);
                    } else {
                        return response()->json([
                            'message' => 'Failed',
                            'status' => false
                        ]);
                    }
                } else {
                    return response()->json([
                        'message' => 'Failed',
                        'status' => false
                    ]);
                }
            } else {
                return response()->json([
                    'message' => 'Failed',
                    'status' => false
                ]);
            }
        }
    }

    public function verificationReport(Request $request)
    {
        //only manager
        $verify = Report::find($request->id);
        $verify->status = $request->status;
        $verify->save();

        if ($verify) {
            return response()->json([
                'message' => 'Success',
                'status' => true
            ]);
        } else {
            return response()->json([
                'message' => 'Failed',
                'status' => false
            ]);
        }
    }

    public function deleteReport(Request $request)
    {
        //only admin & own presence
        $delete = Report::where(
            'id',
            $request->id
        )->delete();

        if ($delete) {
            return response()->json([
                'message' => 'Success',
                'status' => true
            ]);
        } else {
            return response()->json([
                'message' => 'Failed',
                'status' => false
            ]);
        }
    }

    public function createPDF(Request $request)
    {
        $date = $request->date;
        $month = $request->month;
        $year = $request->year;

        if ($date === 'today') {
            $presence = Report::join('users', 'reports.user_id', '=', 'users.id')
                ->where('reports.status', 1)
                ->whereDate('reports.created_at', today())
                ->get(['reports.*', 'users.name']);

            return response()->json([
                'message' => 'success',
                'status' => true,
                'data' => $presence,
            ]);
        } else {
            $presence = Report::join('users', 'reports.user_id', '=', 'users.id')
                ->select('users.name')
                ->selectRaw('count(users.name) as count')
                ->where('reports.status', 1)
                ->whereMonth('reports.created_at', $month)
                ->whereYear('reports.created_at', $year)
                ->groupBy('users.name')
                ->havingRaw('COUNT(users.name) > 0')
                ->get();

            return response()->json([
                'message' => 'success',
                'status' => true,
                'data' => $presence,
            ]);
        }

        return response()->json([
            'message' => 'success',
            'status' => false,
        ]);
    }
}