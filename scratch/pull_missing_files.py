import subprocess
import os

branch = "origin/ui_dangky_dangnhap_nguoidung"

# 1. Java files
java_files = [
    ("java/com/example/projectcuoikiltdd/LoginActivity.java", "app/src/main/java/com/example/projectcuoikiltdd/LoginActivity.java"),
    ("java/com/example/projectcuoikiltdd/RegisterActivity.java", "app/src/main/java/com/example/projectcuoikiltdd/RegisterActivity.java"),
    ("java/com/example/projectcuoikiltdd/ProfileActivity.java", "app/src/main/java/com/example/projectcuoikiltdd/ProfileActivity.java")
]

# 2. Layout files
layout_files = [
    ("res/layout/activity_login.xml", "app/src/main/res/layout/activity_login.xml"),
    ("res/layout/activity_register.xml", "app/src/main/res/layout/activity_register.xml"),
    ("res/layout/activity_profile.xml", "app/src/main/res/layout/activity_profile.xml")
]

# 3. Drawables
res = subprocess.run(["git", "ls-tree", "--name-only", f"{branch}:res/drawable"], capture_output=True, text=True)
drawables = [f.strip() for f in res.stdout.strip().split("\n") if f.strip()]

for remote_path, local_path in java_files + layout_files:
    os.makedirs(os.path.dirname(local_path), exist_ok=True)
    out = subprocess.run(["git", "show", f"{branch}:{remote_path}"], capture_output=True)
    if out.returncode == 0:
        with open(local_path, "wb") as f:
            f.write(out.stdout)
        print(f"Extracted: {local_path}")
    else:
        print(f"Failed to extract: {remote_path}")

for d in drawables:
    local_path = os.path.join("app/src/main/res/drawable", d)
    if not os.path.exists(local_path):
        out = subprocess.run(["git", "show", f"{branch}:res/drawable/{d}"], capture_output=True)
        if out.returncode == 0:
            with open(local_path, "wb") as f:
                f.write(out.stdout)
            print(f"Extracted drawable: {d}")
        else:
            print(f"Failed drawable: {d}")
    else:
        print(f"Already exists: {d}")
