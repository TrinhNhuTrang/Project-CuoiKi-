$ErrorActionPreference = "Stop"

Write-Host "================ CHECKING ALL JAVA FILES ================"
$javaFiles = Get-ChildItem -Path "app\src\main\java" -Recurse -Filter "*.java"
Write-Host "Found $($javaFiles.Count) Java files."

Write-Host "`n================ CHECKING ALL RES XML FILES ================"
$xmlFiles = Get-ChildItem -Path "app\src\main\res" -Recurse -Filter "*.xml"
$xmlErrors = 0
foreach ($f in $xmlFiles) {
    try {
        [xml]$doc = Get-Content $f.FullName -Raw
    } catch {
        Write-Host "ERROR in $($f.FullName): $_" -ForegroundColor Red
        $xmlErrors++
    }
}
if ($xmlErrors -eq 0) {
    Write-Host "All $($xmlFiles.Count) XML files are syntactically VALID!" -ForegroundColor Green
}

Write-Host "`n================ CHECKING DRAWABLE REFERENCES IN LAYOUTS ================"
$layoutFiles = Get-ChildItem -Path "app\src\main\res\layout" -Filter "*.xml"
$missingDrawables = @()
foreach ($l in $layoutFiles) {
    $content = Get-Content $l.FullName -Raw
    $matches = [regex]::Matches($content, '@drawable/([a-zA-Z0-9_]+)')
    foreach ($m in $matches) {
        $d = $m.Groups[1].Value
        $found = (Test-Path "app\src\main\res\drawable\$d.*") -or (Test-Path "app\src\main\res\drawable-v24\$d.*")
        if (-not $found) {
            $missingDrawables += "$($l.Name) -> @drawable/$d"
        }
    }
}
if ($missingDrawables.Count -eq 0) {
    Write-Host "No missing drawables found across all layouts!" -ForegroundColor Green
} else {
    Write-Host "Missing drawables:" -ForegroundColor Red
    $missingDrawables | ForEach-Object { Write-Host "  $_" -ForegroundColor Red }
}

Write-Host "`n================ CHECKING STRING REFERENCES IN LAYOUTS ================"
$stringsXml = Get-Content "app\src\main\res\values\strings.xml" -Raw
$missingStrings = @()
foreach ($l in $layoutFiles) {
    $content = Get-Content $l.FullName -Raw
    $matches = [regex]::Matches($content, '@string/([a-zA-Z0-9_]+)')
    foreach ($m in $matches) {
        $s = $m.Groups[1].Value
        if ($stringsXml -notmatch "name=`"$s`"") {
            $missingStrings += "$($l.Name) -> @string/$s"
        }
    }
}
if ($missingStrings.Count -eq 0) {
    Write-Host "No missing strings found across all layouts!" -ForegroundColor Green
} else {
    Write-Host "Missing strings:" -ForegroundColor Red
    $missingStrings | ForEach-Object { Write-Host "  $_" -ForegroundColor Red }
}

Write-Host "`n================ CHECKING MANIFEST INTEGRITY ================"
[xml]$manifest = Get-Content "app\src\main\AndroidManifest.xml" -Raw
$ns = New-Object System.Xml.XmlNamespaceManager($manifest.NameTable)
$ns.AddNamespace("android", "http://schemas.android.com/apk/res/android")
$activities = $manifest.SelectNodes("//activity", $ns)
foreach ($act in $activities) {
    $name = $act.GetAttribute("name", "http://schemas.android.com/apk/res/android")
    $className = $name.TrimStart('.')
    $javaPath = "app\src\main\java\com\example\projectcuoikiltdd\$className.java"
    if (Test-Path $javaPath) {
        Write-Host "Activity exists: $name" -ForegroundColor Green
    } else {
        Write-Host "Activity class NOT FOUND: $name ($javaPath)" -ForegroundColor Red
    }
}

Write-Host "`n================ HEALTH CHECK COMPLETE ================"
