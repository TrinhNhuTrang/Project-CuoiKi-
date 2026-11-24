$files = Get-ChildItem "app/src/main/res/drawable"
$groups = $files | Group-Object BaseName
foreach ($g in $groups) {
    if ($g.Count -gt 1) {
        Write-Host "Duplicate BaseName: $($g.Name)"
        $g.Group | ForEach-Object { Write-Host "   $($_.Name)" }
    }
}
