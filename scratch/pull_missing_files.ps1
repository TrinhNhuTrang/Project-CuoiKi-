# 1. Checkout files from branch to working tree
git checkout origin/ui_dangky_dangnhap_nguoidung -- res java

# 2. Move Java files: LoginActivity, RegisterActivity, ProfileActivity
$srcJava = "java/com/example/projectcuoikiltdd"
$dstJava = "app/src/main/java/com/example/projectcuoikiltdd"
@("LoginActivity.java", "RegisterActivity.java", "ProfileActivity.java") | ForEach-Object {
    if (Test-Path "$srcJava/$_") {
        Move-Item -Force "$srcJava/$_" "$dstJava/$_"
        Write-Host "Moved Java: $_"
    }
}

# 3. Move Layout files: activity_login.xml, activity_register.xml, activity_profile.xml
$srcLayout = "res/layout"
$dstLayout = "app/src/main/res/layout"
@("activity_login.xml", "activity_register.xml", "activity_profile.xml") | ForEach-Object {
    if (Test-Path "$srcLayout/$_") {
        Move-Item -Force "$srcLayout/$_" "$dstLayout/$_"
        Write-Host "Moved Layout: $_"
    }
}

# 4. Copy missing Drawables
$srcDraw = "res/drawable"
$dstDraw = "app/src/main/res/drawable"
Get-ChildItem -Path $srcDraw | ForEach-Object {
    $target = "$dstDraw/$($_.Name)"
    if (!(Test-Path $target)) {
        Copy-Item -Force $_.FullName $target
        Write-Host "Copied Drawable: $($_.Name)"
    } else {
        Write-Host "Already exists: $($_.Name)"
    }
}

# 5. Clean up temporary root folders
Remove-Item -Recurse -Force res
Remove-Item -Recurse -Force java

# Unstage any accidental additions from git checkout
git reset HEAD
Write-Host "Done!"
