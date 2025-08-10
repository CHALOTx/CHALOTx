#!/bin/bash

set -e

echo "🔍 เริ่มตรวจสอบสถานะ gdm3 และ GUI environment..."

# ฟังก์ชันเช็ค service
check_service() {
  systemctl is-active --quiet "$1"
}

# ฟังก์ชันเช็คแพ็กเกจติดตั้ง
check_package() {
  dpkg -l | grep -qw "$1"
}

# ฟังก์ชันรีสตาร์ท service พร้อมเช็คสถานะ
restart_service() {
  echo "♻️ พยายามรีสตาร์ท $1..."
  sudo systemctl restart "$1"
  sleep 2
  if check_service "$1"; then
    echo "✅ $1 ทำงานปกติ"
  else
    echo "❌ $1 ยังไม่ทำงาน"
    return 1
  fi
}

# ฟังก์ชันรีติดตั้งแพ็กเกจ
reinstall_package() {
  echo "🔄 รีติดตั้งแพ็กเกจ $1"
  sudo apt-get update
  sudo apt-get install --reinstall -y "$1"
}

# ฟังก์ชันตั้งค่า graphical target
set_graphical_target() {
  echo "🎯 ตั้งค่า default target เป็น graphical.target"
  sudo systemctl set-default graphical.target
}

# ฟังก์ชันเปิด GUI session
start_gui() {
  echo "🚀 เริ่ม graphical.target (GUI)..."
  sudo systemctl start graphical.target
}

# ฟังก์ชันตรวจสอบและแก้ไข gdm3
fix_gdm3() {
  if ! check_package "gdm3"; then
    reinstall_package "gdm3"
  fi

  if ! check_package "libglib2.0-0"; then
    reinstall_package "libglib2.0-0"
  fi

  if ! restart_service "gdm3.service"; then
    echo "‼️ gdm3 ยังล่มอยู่ ลองรีติดตั้งใหม่อีกรอบ"
    reinstall_package "gdm3"
    restart_service "gdm3.service" || {
      echo "❌ ยังไม่สามารถเริ่ม gdm3 ได้! แสดง logs:"
      sudo journalctl -xeu gdm3.service -n 20 --no-pager
      exit 1
    }
  fi
}

echo "1. ตรวจสอบ gdm3.service และแพ็กเกจที่จำเป็น..."
fix_gdm3

echo "2. ตั้งค่า default target เป็น graphical.target..."
set_graphical_target

echo "3. เริ่ม GUI session..."
start_gui

echo "4. ตรวจสอบสถานะ gdm3 และ graphical.target..."
if check_service gdm3.service && systemctl is-active --quiet graphical.target; then
  echo "🎉 GDM3 และ GUI พร้อมใช้งานแล้ว!"
else
  echo "⚠️ มีปัญหากับ GDM3 หรือ GUI session โปรดตรวจสอบ logs ต่อไป"
  sudo journalctl -xeu gdm3.service -n 20 --no-pager
fi

echo "✅ สคริปต์เสร็จสิ้น — ลองรีบูตเครื่องแล้วเข้า GUI ดูนะครับ"