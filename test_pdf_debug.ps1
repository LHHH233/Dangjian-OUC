# 测试PDF生成功能
Write-Host "测试PDF生成功能..."

# 测试查询页面
Write-Host "1. 测试查询页面..."
try {
    $response = Invoke-WebRequest -Uri "http://localhost:8080/query" -UseBasicParsing
    Write-Host "查询页面状态: $($response.StatusCode)"
} catch {
    Write-Host "查询页面错误: $($_.Exception.Message)"
}

# 测试POST查询
Write-Host "2. 测试POST查询..."
try {
    $body = @{
        name = "张星雨"
        idCardNumber = "110101199001011234"
    }
    $response = Invoke-WebRequest -Uri "http://localhost:8080/query" -Method POST -Body $body -UseBasicParsing
    Write-Host "POST查询状态: $($response.StatusCode)"
    if ($response.StatusCode -eq 200) {
        Write-Host "查询成功，应该显示党员详细信息页面"
    }
} catch {
    Write-Host "POST查询错误: $($_.Exception.Message)"
}

# 测试PDF生成
Write-Host "3. 测试PDF生成..."
try {
    $response = Invoke-WebRequest -Uri "http://localhost:8080/proof/query?name=张星雨&idCardNumber=110101199001011234" -UseBasicParsing
    Write-Host "PDF生成状态: $($response.StatusCode)"
    if ($response.StatusCode -eq 200) {
        Write-Host "PDF生成成功，大小: $($response.Content.Length) bytes"
        # 保存PDF文件
        $response.Content | Set-Content -Path "test_proof.pdf" -Encoding Byte
        Write-Host "PDF已保存为 test_proof.pdf"
    }
} catch {
    Write-Host "PDF生成错误: $($_.Exception.Message)"
    Write-Host "错误详情: $($_.Exception.Response.StatusCode)"
}

Write-Host "测试完成"




