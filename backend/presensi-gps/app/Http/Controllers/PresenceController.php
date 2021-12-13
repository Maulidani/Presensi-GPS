<?php

namespace App\Http\Controllers;

use App\Models\Office;
use Illuminate\Http\Request;
use App\Models\Presence;
use App\Models\User;
use Illuminate\Support\Facades\Auth;
use Illuminate\Support\Carbon;

class PresenceController extends Controller
{
    public function showPresence(Request $request)
    {
        $userId = $request->user()->id;
        $userPosition = $request->user()->position;

        if ($userPosition === 'sales') {
            $presence = Presence::join('users', 'presences.user_id', '=', 'users.id')
                ->where('presences.user_id', $userId)
                ->orderBy('presences.created_at', 'DESC')
                ->get(['users.image as image_user', 'users.*', 'presences.*']);
        } else {
            $presence = Presence::join('users', 'presences.user_id', '=', 'users.id')
                ->orderBy('presences.created_at', 'DESC')
                ->get(['users.image as image_user', 'users.*', 'presences.*']);
        }

        return response()->json([
            'message' => 'Success',
            'status' => true,
            'data' => $presence,
        ]);
    }

    public function addPresence(Request $request)
    {
        $exist = Presence::where('user_id', '=', $request->user()->id)
            ->whereDate('created_at', today())
            ->exists();

        if ($exist) {
            return response()->json([
                'message' => 'Anda telah mengirim izin/off hari ini',
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

                    $files->move(public_path() . '/image/presence/', $filename);

                    $result = Presence::create([
                        'office_id' => 1,
                        'user_id' => $request->user()->id,
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

    public function offPresence(Request $request)
    {

        $exist = Presence::where('user_id', '=', $request->user()->id)
            ->whereDate('created_at', today())
            ->exists();

        if ($exist) {
            return response()->json([
                'message' => 'Anda telah mengirim presensi hari ini',
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

                    $files->move(public_path() . '/image/presence/', $filename);

                    $result = Presence::create([
                        'office_id' => 1,
                        'user_id' => $request->user()->id,
                        'image' => $filename,
                        'off' => 1,
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

    public function backPresence(Request $request)
    {
        $back = Presence::where('user_id', $request->user()->id)
            ->whereDate('created_at', today())
            ->first();

        $back->forceFill([
            'back_at' => date("H", strtotime('now')),
            'status' => 1,
        ])->save();

        if ($back) {
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

    public function verificationPresence(Request $request)
    {
        //only manager
        $verify = Presence::find($request->id);
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

    public function deletePresence(Request $request)
    {
        //only admin & own presence
        $delete = Presence::where(
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

    public function getLocation(Request $request)
    {
        $office = Office::first();

        return response()->json([
            'message' => 'success',
            'status' => true,
            'data_today' => $office,
        ]);
    }

    public function getPresenceTOday(Request $request)
    {
        $exist = Presence::where('user_id', '=', $request->user()->id)
            ->whereDate('created_at', today())
            ->exists();

        if ($exist) {
            $presence =  Presence::where('user_id', '=', $request->user()->id)
                ->whereDate('created_at', today())
                ->first();

            return response()->json([
                'message' => 'Success',
                'status' => true,
                'data_today' => $presence,
            ]);
        } else {
            return response()->json([
                'message' => 'Failed',
                'status' => false,
                'data_today' => null,
            ]);
        }
    }

    public function createPDF(Request $request)
    {
        $date = $request->date;
        $month = $request->month;
        $year = $request->year;

        if ($date === 'today') {
            $presence = Presence::join('users', 'presences.user_id', '=', 'users.id')
                ->where('presences.status', 1)
                ->where('presences.off', 0)
                ->whereDate('presences.created_at', today())
                ->get(['presences.*', 'users.name']);

            return response()->json([
                'message' => 'success',
                'status' => true,
                'data' => $presence,
            ]);
        } else {
            $presence = Presence::join('users', 'presences.user_id', '=', 'users.id')
                ->select('users.name')
                ->selectRaw('count(users.name) as count')
                ->where('presences.status', 1)
                ->where('presences.off', 0)
                ->whereMonth('presences.created_at', $month)
                ->whereYear('presences.created_at', $year)
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