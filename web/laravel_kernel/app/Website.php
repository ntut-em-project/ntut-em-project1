<?php

namespace App;


use Illuminate\Database\Eloquent\Model;

class Website extends Model
{
    static function getRange() : array{
        $max = static::max('page_rank');
        $min = static::min('page_rank');

        return [
            'max'   =>  $max,
            'min'   =>  $min,
            'diff'  =>  $max - $min,
        ];
    }
}
