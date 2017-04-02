<?php

/*
|--------------------------------------------------------------------------
| Web Routes
|--------------------------------------------------------------------------
|
| Here is where you can register web routes for your application. These
| routes are loaded by the RouteServiceProvider within a group which
| contains the "web" middleware group. Now create something great!
|
*/

use App\Website;
use \Illuminate\Support\Facades\Input;

/*
\Event::listen('Illuminate\Database\Events\QueryExecuted', function ($query) {
    echo '<pre>';
    var_dump($query->sql);
    var_dump($query->bindings);
    var_dump($query->time);
    echo '</pre>';
});
*/

Route::get('/', function () {
    return view('welcome');
});


Route::get('/search', function () {

    $query = request('q');
    $searchArray = [];
    foreach (explode(' ', $query) as $str) {
        $str = trim($str);
        if (!empty($str)) {
            $searchArray[] = $str;
        }
    }

    if (count($searchArray) === 0) {
        return redirect('');
    }

    $websites = Website::select('title', 'url', 'page_rank')
        ->orderBy('page_rank', 'DESC')
        ->where(function ($query) use ($searchArray) {
            foreach ($searchArray as $k) {
                $query->orWhere('title', 'LIKE', '%' . $k . '%');
            }
        });


    $result = $websites->paginate(10)->appends(Input::except('page'));

    return view('search', [
        'result' => $result,
        'input' => $query
    ]);
});
