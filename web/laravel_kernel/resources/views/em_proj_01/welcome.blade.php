<!DOCTYPE html>
<html>
<head>
    <title>NTUT 2017Spring 工程數學專題 PageRank</title>

    @include('em_proj_01.common')

    <link rel="stylesheet" href="{{asset('css/index.min.css')}}">

</head>
<body>
<div class="container">
    <div class="content">
        <div class="title">工程數學專題 PageRank</div>
        <div style="height:32px">
        </div>
        <form class="tsf" action="{{url('em_proj_01/search')}}" style="overflow:visible" id="tsf" method="GET" name="f"
              onsubmit="return q.value!=''" role="search">
            <div class="tsf-p">
                <div class="sfibbbc">
                    <div class="sbtc" id="sbtc">
                        <div class="sbibtd">
                            <div class="nojsv sfopt" id="sfopt">
                                <div class="lsd">
                                    <div id="ss-bar" style="white-space:nowrap;z-index:98" data-jiis="uc"></div>
                                </div>
                            </div>
                            <div class="sbibod " id="sfdiv">
                                <div class="lst-c">
                                    <div class="gstl_0 sbib_a" style="height: 44px;">
                                        <div class="sbib_b" id="sb_ifc0" dir="ltr">
                                            <div id="gs_lc0" style="position: relative;"><input class="gsfi" id="lst-ib"
                                                                                                maxlength="2048"
                                                                                                name="q"
                                                                                                autocomplete="off"
                                                                                                title="搜尋" type="text"
                                                                                                value="" aria-label="搜尋"
                                                                                                aria-haspopup="false"
                                                                                                role="combobox"
                                                                                                aria-autocomplete="both"
                                                                                                dir="ltr"
                                                                                                spellcheck="false"
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
                <div class="jsb" style="padding-top:18px">
                    <center><input value="搜尋" aria-label="搜尋" name="btnK" type="submit" jsaction="sf.chk"></center>
                </div>
            </div></form>
    </div>
    
    <div style="position: absolute;bottom: 0;left: 0;width: 100%;">
        @include('em_proj_01.footer')
    </div>
</div>

</body>
</html>
