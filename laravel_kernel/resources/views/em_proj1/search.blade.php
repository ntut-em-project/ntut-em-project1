<!DOCTYPE html>
<html>
<head>
    <title>{{$input}} - NTUT 2017Spring 工程數學專題 PageRank</title>

    @include('em_proj1.common')

    <link rel="stylesheet" href="{{asset('css/index.min.css')}}">

</head>
<body class="vasq srp">
<div id="searchform" class="jsrp big">
    <div class="sfbg " style="margin-top:-20px">
        <div class="sfbgg" style="height:95px;border-bottom: 1px solid #ebebeb"></div>
    </div>
    <form class="tsf" action="{{url('em_proj1/search')}}" style="overflow:visible" id="tsf" method="GET" name="f"
          onsubmit="return q.value!=''" role="search">
        <div data-jibp="" data-jiis="uc" id="tophf"></div>
        <div class="tsf-p">
            <div class="logocont" id="logocont"><h1><a
                            href="{{url('em_proj1')}}"
                            title="首頁" id="logo" data-hveid="3"><img
                                src="{{asset('image/logo.png')}}" alt=""
                                width="120" ></a></h1></div>
            <div class="sfibbbc">
                <div class="sbtc" id="sbtc">
                    <div class="sbibtd">
                        <div class="sfsbc">
                            <div class="nojsb"></div>
                        </div>
                        <div class="sbibod " id="sfdiv">
                            <div class="lst-c">
                                <div class="gstl_0 sbib_a" style="height: 44px;">
                                    <div class="sbib_b" id="sb_ifc0" dir="ltr">
                                        <div id="gs_lc0" style="position: relative;"><input class="gsfi" id="lst-ib"
                                                                                            maxlength="2048" name="q"
                                                                                            autocomplete="off"
                                                                                            title="搜尋" type="text"
                                                                                            value="{{$input}}"
                                                                                            aria-label="搜尋"
                                                                                            aria-haspopup="false"
                                                                                            role="combobox"
                                                                                            aria-autocomplete="both"
                                                                                            dir="ltr" spellcheck="false"
                                                                                            style="border: none; padding: 0; margin: 0; height: auto; width: 100%; background: url(&quot;data:image/gif;base64,R0lGODlhAQABAID/AMDAwAAAACH5BAEAAAAALAAAAAABAAEAAAICRAEAOw%3D%3D&quot;) transparent; position: absolute; z-index: 6; left: 0; outline: none;">
                                            <div class="gsfi" id="gs_sc0"
                                                 style="background: transparent; color: transparent; padding: 0; position: absolute; z-index: 2; white-space: pre; visibility: hidden;"></div>
                                            <input class="gsfi" disabled="" autocomplete="off" autocapitalize="off"
                                                   aria-hidden="true" id="gs_taif0" dir="ltr"
                                                   style="border: none; padding: 0; margin: 0; height: auto; width: 100%; position: absolute; z-index: 1; background-color: transparent; -webkit-text-fill-color: silver; color: silver; left: 0;"><input
                                                    class="gsfi" disabled="" autocomplete="off" autocapitalize="off"
                                                    aria-hidden="true" id="gs_htif0" dir="ltr"
                                                    style="border: none; padding: 0; margin: 0; height: auto; width: 100%; position: absolute; z-index: 1; background-color: transparent; -webkit-text-fill-color: silver; color: silver; transition: all 0.218s; opacity: 0; text-align: left; left: 0;">
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="gsfi"
             style="background: transparent; color: rgb(0, 0, 0); padding: 0; position: absolute; white-space: pre; visibility: hidden;"></div>
        <input type="hidden" name="oq"><input type="hidden" name="gs_l">
        <div class="jsb" style="padding-top:18px">
            <center><input value="搜尋" aria-label="搜尋" name="btnK" type="submit" jsaction="sf.chk"></center>
        </div></form>
</div>
<div class="search-container">
    <div class="content">
        @if(count($result)===0)
            <div class="mnr-c">
                <div class="med card-section"><p style="padding-top:.33em"> 找不到符合搜尋字詞「<em>{{$input}}</em>」的網頁。
                    </p>
                    <p style="margin-top:1em">建議：</p>
                    <ul style="margin-left:1.3em;margin-bottom:2em">
                        <li>請檢查有無錯別字</li>
                        <li>試試以其他關鍵字搜尋。</li>
                        <li>試試 <a href="https://www.google.com.tw/search?q={{urlencode($input)}}" target="_blank">Google 搜尋</a></li>
                    </ul>
                </div>
            </div>
        @endif
        @foreach ($result as $web)
            <div class="result">
                <h3>
                    <a href="{{ $web->url}}" target="_blank">{{$web->title}}</a>
                </h3>
                <p>
                    <cite>{{ $web->url}}</cite>
                    <cite><b>PageRank:</b> {{number_format(($web->page_rank - $range['min']) * 10 / $range['diff'], 30)}}</cite>
                </p>
            </div>
        @endforeach
    </div>

    <div class="links">
        {{ $result->links() }}
    </div>

</div>
@include('em_proj1.footer')
</body>
</html>
