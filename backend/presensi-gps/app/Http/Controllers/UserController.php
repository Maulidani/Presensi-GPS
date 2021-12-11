<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\Models\User;
use Illuminate\Support\Facades\Auth;

class UserController extends Controller
{
    public function index(Request $request)
    {
        return response()->json([
            'message' => 'Success',
            'status' => true,
            'user' => $request->user()
        ]);
    }

    public function register(Request $request)
    {
        $request->validate([
            'name' => 'required',
            'email' => 'required|email|unique:users',
            'password' => 'required',
        ]);

        $files = $request->image;
        $allowedfileExtension = ['jpeg', 'jpg', 'png', 'JPG', 'JPEG'];
        if ($request->hasfile('image')) {

            $filename = time() . '.' . $files->getClientOriginalName();
            $extension = $files->getClientOriginalExtension();

            $check = in_array($extension, $allowedfileExtension);

            if ($check) {

                $files->move(public_path() . '/image/user/', $filename);

                $result = User::create([
                    'name' => $request->name,
                    'position' => $request->position,
                    'email' => $request->email,
                    'password' => $request->password,
                    'image' => $filename
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

    public function login(Request $request)
    {
        // $credentials = $request->validate([
        //     'email' => 'required|email',
        //     'password' => 'required'
        // ]);

        $auth = User::where('email', $request->email)
            ->where('password', $request->password)
            ->first();

        if ($auth) {
            $token = md5(time()) . '.' . md5($request->email);
            $auth->forceFill([
                'api_token' => $token
            ])->save();

            return response()->json([
                'message' => 'Success',
                'status' => true,
                'token' => $token
            ]);
        } else {
            return response()->json([
                'message' => 'Failed',
                'status' => false
            ]);
        }
    }

    public function logout(Request $request)
    {
        $logout = $request->user()->forceFill([
            'api_token' => null
        ])->save();

        if ($logout) {
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

    public function edit(Request $request)
    {

        $user = User::find($request->id);
        $user->name = $request->name;
        $user->position = $request->position;
        $user->email = $request->email;
        $user->password = $request->password;
        $user->save();

        if ($user) {
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
    public function editImage(Request $request)
    {

        $files = $request->image;
        $allowedfileExtension = ['jpeg', 'jpg', 'png', 'JPG', 'JPEG'];
        if ($request->hasfile('image')) {

            $filename = time() . '.' . $files->getClientOriginalName();
            $extension = $files->getClientOriginalExtension();

            $check = in_array($extension, $allowedfileExtension);

            if ($check) {

                $files->move(public_path() . '/image/user/', $filename);

                $user = User::find($request->id);
                $user->image = $filename;
                $user->save();

                if ($user) {
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
        } else {
            return response()->json([
                'message' => 'Failed',
                'status' => false
            ]);
        }
    }

    public function deleteUser(Request $request)
    {
        $user = User::where(
            'id',
            $request->id
        )->delete();

        if ($user) {
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

    public function showUser(Request $request)
    {
        $user = User::where('position', $request->position)
            ->orderBy('created_at', 'DESC')
            ->get();

        return response()->json([
            'message' => 'Success',
            'status' => true,
            'list_user' => $user
        ]);
    }
}